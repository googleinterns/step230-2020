
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
* This class creates objects that are passed from a servlet
* to the client and keep information needed for building
* the postcard. 
**/

package com.google.sps.data;

public final class Output {

  private final String text;
  private final String link; 

  public Output(String text, String link) {
    this.text = text;
    this.link = link;
  }
}