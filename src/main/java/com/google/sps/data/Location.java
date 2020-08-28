
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

public final class Location {

  private final double latitude;
  private final double longitude;

  public Location(String location) {
    int start = 10;
    int end = location.lastIndexOf("Longitude") - 1;
    this.latitude = Double.parseDouble(location.substring(start, end));
    start = end + 12;
    this.longitude = Double.parseDouble(location.substring(start));
  }

  public double getLongitude() {
    return this.longitude;
  }

  public double getLatitude() {
    return this.latitude;
  }
}