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
import java.io.FileInputStream;
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
  
  public AnnotateImageRequest localImageAnnotationRequest(String imagePath, 
                                                          Type analysis) throws IOException {
    ByteString imageBytes = ByteString.readFrom(new FileInputStream(imagePath));
    Image image = Image.newBuilder().setContent(imageBytes).build();
    Feature feature = Feature.newBuilder().setType(analysis).build();

    return AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
  }

  public AnnotateImageRequest remoteImageAnnotationRequest(String imageUrl, 
                                                            Type analysis) throws IOException {
    ImageSource imageSource = ImageSource.newBuilder().setImageUri(imageUrl).build();
    Image image = Image.newBuilder().setSource(imageSource).build();
    Feature feature = Feature.newBuilder().setType(analysis).build();
    
    return AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
  }

  public List<AnnotateImageResponse> getResponses(String imageUrl, Type analysis) {
    List<AnnotateImageResponse> responses;

    try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(getSettings())) {
      List<AnnotateImageRequest> requests = new ArrayList<>();
      requests.add(remoteImageAnnotationRequest(imageUrl, analysis));

      BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
      responses = response.getResponsesList();
    } catch(Exception ex) {
      responses = new ArrayList<>();
      System.out.println(ex);
    }
    return responses;
  }

  public List<AnnotateImageResponse> getResponsesStoredImage(String imagePath, Type analysis) {
    List<AnnotateImageResponse> responses;

    try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(getSettings())) {
      List<AnnotateImageRequest> requests = new ArrayList<>();
      requests.add(localImageAnnotationRequest(imagePath, analysis));

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

  /**
   * Analyses the elements inside the picture.
   * @param   imageUrl Link of image that needs to be analysed.
   * @return  List of elements that appear in the picture.
   */
  public abstract List<String> analyseStoredImage(String imagePath);
}
