package com.google.sps.image;

import com.google.api.gax.rpc.HeaderProvider;
import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.cloud.vision.v1.ImageSource;
import com.google.cloud.vision.v1.LocationInfo;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Analyser {

  public static ImageAnnotatorSettings getSettings() throws IOException {
    HeaderProvider headerProvider =
            FixedHeaderProvider.create("X-Goog-User-Project","google.com:gpostcard");
    return ImageAnnotatorSettings.newBuilder().setHeaderProvider(headerProvider).build();
  }
  
  public List<AnnotateImageResponse> getResponses(String imageUrl, Type analysis) {
    List<AnnotateImageResponse> responses;

    try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(getSettings())) {
      // Builds the image annotation request
      List<AnnotateImageRequest> requests = new ArrayList<>();
      ImageSource imageSource = ImageSource.newBuilder().setImageUri(imageUrl).build();
      Image img = Image.newBuilder().setSource(imageSource).build();
      Feature label = Feature.newBuilder().setType(analysis).build();
      AnnotateImageRequest labelRequest =
          AnnotateImageRequest.newBuilder().addFeatures(label).setImage(img).build();
      requests.add(labelRequest);

      // Performs label detection on the image file
      BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
      responses = response.getResponsesList();
    } catch(Exception ex) {
      responses = new ArrayList<>();
      System.out.println(ex);
    }
    return responses;
  }

  /**
   * Analyses the elements inside the picture.
   * @param   imageUrl Link of image that needs to be analysed.
   * @return  List of elements that appear in the picture.
   */
  public abstract List<String> analyse(String imageUrl);
}
