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

import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public final class TextAnalyser {
    private final String message;
    private final ArrayList<String> keyWords = new ArrayList<String>();

    public TextAnalyser(String message) {
        this.message = message;
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
        return "super super happy";
      }
 
      if(score == -1) {
        return "so pessimistic";
      }
 
      int position = 0;
      String[] moods = new String[]{"neutral", "calm", "relaxed", "serene", "contented",
                                  "joyful", "happy", "elated", "excited", "alert",
                                  "tense", "nervous", "stressed", "upset", "sad", 
                                  "depressed", "bored", "fatigued", "pessimisctic"};
 
      score = score * 10;
      position = (int) score;
 
      if(position < 0) {
        position = position * (-1) + 9;
      }
 
      return moods[position];
    }

    public ClassifyTextResponse classify() {
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
          categories.add(listCategories[i]);
        }
      }
      return categories;
    }

    public void addEvents() {
      String[] events = new String[]{"birthday", "wedding", "baby shower", "love",
                                     "congratulation", "travel", "good morning",
                                     "graduation", "gratitude"};
      String copy = new String(message);
      copy = copy.toLowerCase();

      for (int i = 0; i < events.length; i++) {
        if(copy.indexOf(events[i]) != -1) {
            keyWords.add(events[i]);
        }
      }
    }

    public ArrayList<String> getKeyWords() throws IOException {
      keyWords.add(getMood());
      addEvents();
      for(String category : getCategories()) {
        keyWords.add(category);
      }

      return keyWords;
    }
}