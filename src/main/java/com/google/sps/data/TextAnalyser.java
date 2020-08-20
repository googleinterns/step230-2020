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
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* This class creates objects that analyse text and 
*/
public final class TextAnalyser {
  private final String message;

  public TextAnalyser(String message) {
     this.message = message.toLowerCase();
  }

  public float getSentimentScore() throws IOException {
    try (LanguageServiceClient languageService = LanguageServiceClient.create()) {
      Document doc =
        Document.newBuilder().setContent(message).setType(Document.Type.PLAIN_TEXT).build();
      Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
        
      return sentiment.getScore();
    }
  }

  public String getMood() throws IOException {
    float score = getSentimentScore();
    
    if(score == 1) {
      return "very happy";
    }
 
    if(score == -1) {
      return "so pessimistic";
    }
 
    int position = 0;
    String[] moods = new String[]{"neutral", "calm", "relaxed", "serene", "contented",
                                  "joyful", "happy", "delighted", "excited", "thrilled",
                                  "tense", "nervous", "stressed", "upset", "sad", 
                                  "depressed", "bored", "fatigued", "pessimisctic"};
 
    score = score * 10;
    position = (int) score;
 
    if(position < 0) {
      position = position * (-1) + 9;
    }
 
    return moods[position];
  }

  private ClassifyTextResponse classify() {
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document document = 
            Document.newBuilder().setContent(message).setType(Type.PLAIN_TEXT).build();
      ClassifyTextRequest request = ClassifyTextRequest.newBuilder().setDocument(document).build();
        
      return language.classifyText(request);
    } catch(Exception e) {
      return null;
    }
  }

  // list of all the categories that text is about
  // check https://cloud.google.com/natural-language/docs/categories
  public List<String> getCategories() {
    ClassifyTextResponse response = classify();

    if(response == null) {
      return Collections.emptyList();
    }
      
    ArrayList<String> categories = new ArrayList<String>();

    for (ClassificationCategory category : response.getCategoriesList()) {
      String[] listCategories = category.getName().split("/");
      for (int i = 0; i < listCategories.length; i++) {
        categories.add(listCategories[i].toLowerCase());
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
    String copy = new String(message);
    copy = copy.toLowerCase();

    for (int i = 0; i < allEvents.length; i++) {
      if(copy.indexOf(allEvents[i]) != -1) {
        events.add(allEvents[i]);
      }
    }

    return events;
  }

  /** Identifies entities in the string */
  private AnalyzeEntitiesResponse analyzeEntitiesText() throws IOException {
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder().setContent(message).setType(Type.PLAIN_TEXT).build();
      AnalyzeEntitiesRequest request =
          AnalyzeEntitiesRequest.newBuilder()
              .setDocument(doc)
              .setEncodingType(EncodingType.UTF16)
              .build();

      return language.analyzeEntities(request);
    }
  }

  public List<String> getEntities() throws IOException {
    AnalyzeEntitiesResponse response = analyzeEntitiesText();
    List<String> entities = new ArrayList<String>();

    for (Entity entity : response.getEntitiesList()) {
      entities.add(entity.getName());
    }

    return entities;
  }

  // put all the key words together
  // use a HashSet to remove duplicates
  public Set<String> getKeyWords() throws IOException {
    Set<String> keyWords = new HashSet<String>();

    keyWords.addAll(getEvents());
    keyWords.addAll(getEntities());
    keyWords.addAll(getCategories());
    keyWords.add(getMood());

    return keyWords;
  }
}