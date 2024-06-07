function image_sepia = sepia(original)
  % Sepia effect emphasises the Red and Green channels of an image and reduces the emphasis on the Blue channel
  % -> Applies a warm, brownish tone to the original image
  % -> Simply multiply rows of Sepia filter matrix with each corresponding colour channel of original image (matrix multiplication)

  % Define the sepia matrix
  sepia_matrix = [0.393, 0.769, 0.189;
                 0.349, 0.686, 0.168;
                 0.272, 0.534, 0.131];

   % Check if the original image is a 16x16 matrix (pixels) or less
   [height, width] = size(original);
   % NOTE: Width will be 3x the width of the image as the number of pixels (eg. 16) will be multiplied by the 3 RGB colour channels (eg. 16*3 = 48)

   if height <= 16 && (width / 3) <= 16
      disp(char(10)); % New line
      disp('Original RGB Image:');
      disp(original);
      disp(char(10)); % New line
      disp('Original RGB Image --> Sepia RGB Image');
      disp('Multiplying each colour channel individually by Sepia filter matrix');
      disp('The following operations will be performed:');
      disp('Sepia Matrix ROW 1 * RED Channel');
      disp('Sepia Matrix ROW 2 * GREEN Channel');
      disp('Sepia Matrix ROW 3 * BLUE Channel');
      disp(char(10)); % New line

      disp('Original Red Channel:');
      disp(original(:,:,1));
      disp(char(10)); % New line
      disp('Result of Sepia filter on Red Channel:');
      disp((sepia_matrix(1,1).* original(:,:,1)) + (sepia_matrix(1,2).* original(:,:,1)) + (sepia_matrix(1,3).* original(:,:,1)));
      disp(char(10)); % New line

      disp('Original Green Channel:');
      disp(original(:,:,2));
      disp(char(10)); % New line
      disp('Result of Sepia filter on Green Channel:');
      disp((sepia_matrix(2,1).* original(:,:,2)) + (sepia_matrix(2,2).* original(:,:,2)) + (sepia_matrix(2,3).* original(:,:,2)));
      disp(char(10)); % New line

      disp('Original Blue Channel:');
      disp(original(:,:,3));
      disp(char(10)); % New line
      disp('Result of Sepia filter on Blue Channel:');
      disp((sepia_matrix(3,1).* original(:,:,3)) + (sepia_matrix(3,2).* original(:,:,3)) + (sepia_matrix(3,3).* original(:,:,3)));
      disp(char(10)); % New line
   else
      disp('Image size too large to display matrix operations. Please use an image that is 16x16 pixels or less in size. ');
   endif

   output_red = (sepia_matrix(1,1).* original(:,:,1)) + (sepia_matrix(1,2).* original(:,:,1)) + (sepia_matrix(1,3).* original(:,:,1));
   output_green = (sepia_matrix(2,1).* original(:,:,2)) + (sepia_matrix(2,2).* original(:,:,2)) + (sepia_matrix(2,3).* original(:,:,2));
   output_blue = (sepia_matrix(3,1).* original(:,:,3)) + (sepia_matrix(3,2).* original(:,:,3)) + (sepia_matrix(3,3).* original(:,:,3));

   image_sepia = uint8(cat(3, output_red, output_green, output_blue)); % Concatenate the three new Sepia-filter-applied colour channels
end
