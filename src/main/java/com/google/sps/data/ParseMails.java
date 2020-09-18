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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
* This class creates objects that parse a string 
* with multiple emails separated by space symbol and
* return a list of emails where to send a posrcard
**/

public final class ParseMails {

  private List<String> mails = new ArrayList<String>();

  public ParseMails(String mail) {
    String[] mailsArray = mail.split("\\s");
    this.mails = Arrays.asList(mailsArray);  
  }

  public List<String> getMails() {
    return this.mails;
  }
}