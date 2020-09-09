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
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class ImageAnalyser {

  public static ImageAnnotatorSettings getSettings() throws IOException {
    HeaderProvider headerProvider =
            FixedHeaderProvider.create("X-Goog-User-Project","google.com:gpostcard");
    return ImageAnnotatorSettings.newBuilder().setHeaderProvider(headerProvider).build();
  }

  public static void analyse(String imageUrl) {
    try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(getSettings())) {
      // Builds the image annotation request
      List<AnnotateImageRequest> requests = new ArrayList<>();
      ImageSource imageSource = ImageSource.newBuilder().setImageUri(imageUrl).build();
      Image img = Image.newBuilder().setSource(imageSource).build();
      Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
      AnnotateImageRequest request =
          AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
      requests.add(request);

      // Performs label detection on the image file
      BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
      List<AnnotateImageResponse> responses = response.getResponsesList();

      for (AnnotateImageResponse res : responses) {
        if (res.hasError()) {
          System.out.format("Error: %s%n", res.getError().getMessage());
          return;
        }

        for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
          annotation
              .getAllFields()
              .forEach((k, v) -> System.out.format("%s : %s%n", k, v.toString()));
        }
      }
    } catch(Exception ex) {
      System.out.println(ex);
    }
  }
}