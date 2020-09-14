// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.api.gax.rpc.HeaderProvider;
import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
* This class creates objects that analyse text by 
* extracting the categories, the mood, the events.
* The scope is to get a final set of the key words
* from the text. 
**/

public final class TextAnalyser {
  private final String message;
  private static final double EPSILON = 0.00001;

  public TextAnalyser(String message) {
     this.message = message.toLowerCase();
  }

  public final LanguageServiceSettings getSettings() throws IOException {
    HeaderProvider headerProvider =
            FixedHeaderProvider.create("X-Goog-User-Project","google.com:gpostcard");
    return LanguageServiceSettings.newBuilder()
                .setHeaderProvider(headerProvider)
                .build();
  }

  public float getSentimentScore() throws IOException {
    try (LanguageServiceClient languageService = LanguageServiceClient.create(getSettings())) {
      Document doc =
        Document.newBuilder().setContent(message).setType(Document.Type.PLAIN_TEXT).build();
      Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
        
      return sentiment.getScore();
    }
  }

  public String getMood() throws IOException {
    float score = getSentimentScore();

    if (Math.abs(score - 1) < EPSILON) {
      return "very happy";
    }
 
    if (Math.abs(score + 1) < EPSILON) {
      return "so pessimistic";
    }

    /**
    * From (-1, 1) the words are displayed on the x axis based on the sentiment score like this:
    *
    * pessimistic (-0.9), fatigued (-0.8), bored, depressed, sad, upset, stressed, nervous, tense (-0.1),
    * neutral(0.0), calm (0.1), relaxed, serene, contented, joyful, happy, delighted, excited, thrilled (0.9)
    *
    * If the score is >= 0 then the mood is at position (int) (score * 10) => first 10 moods are ordered
    * based on the sentiment score of the text from 0.0 to 0.9 (0.1 incrementation)
    *
    * If the score is negative, return the mood at position (int) (score * 10) * (-1) + 9 => next 9 moods are ordered
    * based on the sentiment score of the text from -0.1 to -0.9 (-0.1 incrementation)
    **/
 
    int position = 0;
    String[] moods = new String[] {"neutral", "calm", "relaxed", "serene", "contented",
                                    "joyful", "happy", "delighted", "excited", "happy",
                                   "tense", "nervous", "stressed", "upset", "sad", 
                                   "depressed", "bored", "fatigued", "pessimistic"};

    assert moods.length == 19 : "There can only be 19 moods.";

    position = (int) (score * 10);
 
    if (position < 0) {
      position = position * (-1) + 9;
    }
 
    return moods[position];
  }

  private ClassifyTextResponse classify() throws InvalidArgumentException, IOException {
    try (LanguageServiceClient language = LanguageServiceClient.create(getSettings())) {
      Document document = 
            Document.newBuilder().setContent(message).setType(Type.PLAIN_TEXT).build();
      ClassifyTextRequest request = ClassifyTextRequest.newBuilder().setDocument(document).build();
      
      return language.classifyText(request);
    }
  }

  // list of all the categories that text is about
  // check https://cloud.google.com/natural-language/docs/categories
  public Set<String> getCategories() throws IOException {
    try {
      Set<String> categories = new LinkedHashSet<String>();

      for (ClassificationCategory category : classify().getCategoriesList()) {
        String[] listCategories = category.getName().split("/");
        categories.add(listCategories[listCategories.length - 1].toLowerCase());
      }

      return categories;
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
      return Collections.emptySet();
    }
  }

  public Set<String> getEvents() {
    Set<String> events = new LinkedHashSet<String>();
    String[] allEvents = new String[] {"wedding", "travel", "promotion", "graduation", 
                                       "funeral", "party"};

    for (int i = 0; i < allEvents.length; i++) {
      if (message.indexOf(allEvents[i]) != -1) {
        events.add(allEvents[i]);
      }
    }

    return events;
  }

  public Set<String> getGreetings() {
    Set<String> greetings = new LinkedHashSet<String>();
    String[] allGreetings = new String[] {"good morning", "welcome", "good evening", 
                                          "good night", "good afternoon", "hello", "hey",
                                          "happy birthday", "love you"};
    
    for (int i = 0; i < allGreetings.length; i++) {
      if (message.indexOf(allGreetings[i]) != -1) {
        greetings.add(allGreetings[i]);
      }
    }

    return greetings;
  }

  /** Identifies entities in the string */
  private AnalyzeEntitiesResponse analyzeEntitiesText() throws IOException {
    try (LanguageServiceClient language = LanguageServiceClient.create(getSettings())) {
      Document doc = Document.newBuilder().setContent(message).setType(Type.PLAIN_TEXT).build();
      AnalyzeEntitiesRequest request =
          AnalyzeEntitiesRequest.newBuilder()
              .setDocument(doc)
              .setEncodingType(EncodingType.UTF16)
              .build();
      return language.analyzeEntities(request);
    }
  }

  public Set<String> getEntities() throws IOException {
    AnalyzeEntitiesResponse response = analyzeEntitiesText();
    Set<String> entities = new LinkedHashSet<String>();

    for (Entity entity : response.getEntitiesList()) {
      entities.add(entity.getName());
    }

    return entities;
  }

  public boolean isInjection() {
    if (message.indexOf("<script>") != -1 || message.indexOf("</script>") != -1 ||
        message.indexOf("<html>") != -1 || message.indexOf("</html>") != -1) {
      return true;
    }

    return false;
  }

  // Put all the key words together
  // Use a LinkedHashSet to remove duplicates but maintain order
  public Set<String> getKeyWords() {
    try {
      Set<String> keyWords = new LinkedHashSet<String>();

      keyWords.addAll(getGreetings());
      keyWords.addAll(getEvents());
      keyWords.addAll(getEntities());
      keyWords.addAll(getCategories());

      return keyWords;
    } catch (IOException e) {
      // No key words  
      System.err.println("There are no key words.");
      e.printStackTrace();
      return Collections.emptySet();
    }
  }

  public Set<String[]> getSetsOfKeyWords() {
    Set< String[] > setsOfKeyWords = new LinkedHashSet<>();
    List<String> keyWords = new ArrayList<String>(getKeyWords());

    // Only returns one key word when only a sentiment is found
    if(keyWords.size() == 1) {
      setsOfKeyWords.add(new String[] {keyWords.get(0)});
      return setsOfKeyWords;
    }

    /**
    * TODO: Add default value when no keyword is found
    * and discuss with team
    **/

    for (int i = 0; i < keyWords.size(); i++) {
      for (int j = i + 1; j < keyWords.size(); j++) {
        setsOfKeyWords.add(new String[] {keyWords.get(i), keyWords.get(j)});
      }
    }

    return setsOfKeyWords;
  }
}