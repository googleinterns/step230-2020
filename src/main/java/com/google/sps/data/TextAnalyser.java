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


import com.google.api.gax.rpc.HeaderProvider;
import com.google.api.gax.rpc.FixedHeaderProvider;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* This class creates objects that analyse text by 
* extracting the categories, the mood, the events.
* The scope is to get a final set of the key words
* from the text. 
*/

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
 
    int position = 0;
    String[] moods = new String[]{"neutral", "calm", "relaxed", "serene", "contented",
                                  "joyful", "happy", "delighted", "excited", "thrilled",
                                  "tense", "nervous", "stressed", "upset", "sad", 
                                  "depressed", "bored", "fatigued", "pessimisctic"};
 
    score = score * 10;
    position = (int) score;
 
    if (position < 0) {
      position = position * (-1) + 9;
    }
 
    return moods[position];
  }

  private ClassifyTextResponse classify() throws IOException {
    try (LanguageServiceClient language = LanguageServiceClient.create(getSettings())) {
      Document document = 
            Document.newBuilder().setContent(message).setType(Type.PLAIN_TEXT).build();
      ClassifyTextRequest request = ClassifyTextRequest.newBuilder().setDocument(document).build();
      
      return language.classifyText(request);
    } catch (Exception e) {
      System.err.println("Not enough tokens (words) to actually get a category.");
      return null;
    }
  }

  // list of all the categories that text is about
  // check https://cloud.google.com/natural-language/docs/categories
  public Set<String> getCategories() throws IOException {
    ClassifyTextResponse response = classify();

    // no categories could be identified
    if (response == null) {
      return Collections.emptySet();
    }
      
    Set<String> categories = new HashSet<String>();

    for (ClassificationCategory category : response.getCategoriesList()) {
      String[] listCategories = category.getName().split("/");
      for (int i = 0; i < listCategories.length; i++) {
        categories.add(listCategories[i].toLowerCase());
        System.out.println(listCategories[i].toLowerCase());
      }
    }

    return categories;
  }

  public Set<String> getEvents() {
    Set<String> events = new HashSet<String>();
    String[] allEvents = new String[]{"birthday", "wedding", "baby shower", "love",
                                    "congratulation", "travel", "good morning",
                                    "gratitude", "job", "promotion",
                                    "new", "welcome", "good evening", "good night",
                                    "holiday"};
    String copy = message;

    for (int i = 0; i < allEvents.length; i++) {
      if (copy.indexOf(allEvents[i]) != -1) {
        events.add(allEvents[i]);
      }
    }

    return events;
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
    Set<String> entities = new HashSet<String>();

    for (Entity entity : response.getEntitiesList()) {
      entities.add(entity.getName());
    }

    return entities;
  }

  // put all the key words together
  // use a HashSet to remove duplicates
  public Set<String> getKeyWords() {
    try {
      Set<String> keyWords = new HashSet<String>();
      keyWords.addAll(getEvents());
      keyWords.addAll(getEntities());
      keyWords.addAll(getCategories());
      keyWords.add(getMood());

      return keyWords;
    } catch (IOException e) {
      // no key words  
      System.err.println("There are no key words.");
      return Collections.emptySet();
    }
  }
}