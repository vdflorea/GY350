function image_inv = invert(original)
  % Subtract each individual Red, Green and Blue channel entry from 255 (matrix subtraction)

   % Check if the original image is a 16x16 matrix (pixels) or less
   [height, width] = size(original);
   % NOTE: Width will be 3x the width of the image as the number of pixels (eg. 16) will be multiplied by the 3 RGB colour channels (eg. 16*3 = 48)

   if height <= 16 && (width / 3) <= 16
      disp(char(10)); % New line
      disp('Original RGB Image:');
      disp(original);
      disp(char(10)); % New line
      disp('Original RGB Image --> Inverted RGB Image');
      disp('Subtracting maximum uint8 value (255) from each colour channel');
      disp('The following operations will be performed:');
      disp('255 - RED Channel');
      disp('255 - GREEN Channel');
      disp('255 - BLUE Channel');
      disp(char(10)); % New line

      disp('Original Red Channel:');
      disp(original(:,:,1));
      disp(char(10)); % New line
      disp('Result of inversion on Red Channel (Subtracted maximum value 255):');
      disp(255 .- original(:,:,1));
      disp(char(10)); % New line

      disp('Original Green Channel:');
      disp(original(:,:,2));
      disp(char(10)); % New line
      disp('Result of inversion on Green Channel (Subtracted maximum value 255):');
      disp(255 .- original(:,:,2));
      disp(char(10)); % New line

      disp('Original Blue Channel:');
      disp(original(:,:,3));
      disp(char(10)); % New line
      disp('Result of inversion on Blue Channel (Subtracted maximum value 255):');
      disp(255 .- original(:,:,3));
      disp(char(10)); % New line
   else
      disp('Image size too large to display matrix operations. Please use an image that is 16x16 pixels or less in size. ');
   endif

   image_inv = 255 - original; % All three channels simultaneously
end
