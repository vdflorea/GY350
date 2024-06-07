function image_green = green_channel(original)
  % Since each image has three colour channels, we can extract one of the colour channels by multiplying itself by 1 and the other two by 0 (matrix multiplication)
  % -> To extract GREEN Channel: original .* [1, 0, 0]

   % Check if the original image is a 16x16 matrix (pixels) or less
   [height, width] = size(original);
   % NOTE: Width will be 3x the width of the image as the number of pixels (eg. 16) will be multiplied by the 3 RGB colour channels (eg. 16*3 = 48)

   if height <= 16 && (width / 3) <= 16
      disp(char(10)); % New line
      disp('Original RGB Image:');
      disp(original);
      disp(char(10)); % New line
      disp('Extract GREEN Channel of original RGB image');
      disp('The following operation will be performed:');
      disp('Multiply original image by [0, 1, 0] to extract GREEN Channel');
      disp(char(10)); % New line

      disp('Original Red Channel:');
      disp(original(:,:,1));
      disp(char(10)); % New line
      disp('Result of GREEN Channel extraction on Red Channel:');
      disp(original(:,:,1) .* 0); % All pixels stay the same
      disp(char(10)); % New line

      disp('Original Green Channel:');
      disp(original(:,:,2));
      disp(char(10)); % New line
      disp('Result of GREEN Channel extraction on Green Channel:');
      disp(original(:,:,2) .* 1); % All pixels = 0
      disp(char(10)); % New line

      disp('Original Blue Channel:');
      disp(original(:,:,3));
      disp(char(10)); % New line
      disp('Result of GREEN Channel extraction on Blue Channel:');
      disp(original(:,:,3) .* 0); % All pixels = 0
      disp(char(10)); % New line
   else
      disp('Image size too large to display matrix operations. Please use an image that is 16x16 pixels or less in size. ');
   endif

   output_red = original(:,:,1) .* 0;
   output_green = original(:,:,2) .* 1;
   output_blue = original(:,:,3) .* 0;

   image_green = uint8(cat(3, output_red, output_green, output_blue));
end
