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

/**
* This class creates objects that present authentication status
* of the user in order to manipulate the DOM
**/

public final class Login {

  private final boolean status;
  private final String message;

  public Login(boolean status, String message) {
    this.status = status;
    this.message = message;
  }
}