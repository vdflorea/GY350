function image_gs = grayscale(original)
  % Convert picture to grayscale using NTSC standard
  % -> Multiply Red, Green and Blue channel matrices by alpha, beta, gamma scalars respectively

   alpha = 0.2989;
   beta = 0.587;
   gamma = 0.114;

   % Check if the original image is a 16x16 matrix (pixels) or less
   [height, width] = size(original);
   % NOTE: Width will be 3x the width of the image as the number of pixels (eg. 16) will be multiplied by the 3 RGB colour channels (eg. 16*3 = 48)

   if height <= 16 && (width / 3) <= 16
      disp(char(10)); % New line
      disp('Original RGB Image:');
      disp(original);
      disp(char(10)); % New line
      disp('Original RGB Image --> Grayscale Image');
      disp('Applying the NTSC standard to each colour channel individually');
      disp('The following operations will be performed:');
      disp('RED Channel * 0.2989');
      disp('GREEN Channel * 0.587');
      disp('BLUE Channel * 0.114');
      disp(char(10)); % New line

      disp('Original Red Channel:');
      disp(original(:,:,1));
      disp(char(10)); % New line
      disp('Result of NTSC Standard on Red Channel (Multiplied by scalar -> 0.2989):');
      disp(alpha .* original(:,:,1));
      disp(char(10)); % New line

      disp('Original Green Channel:');
      disp(original(:,:,2));
      disp(char(10)); % New line
      disp('Result of NTSC Standard on Green Channel (Multiplied by scalar -> 0.587):');
      disp(beta .* original(:,:,2));
      disp(char(10)); % New line

      disp('Original Blue Channel:');
      disp(original(:,:,3));
      disp(char(10)); % New line
      disp('Result of NTSC Standard on Blue Channel (Multiplied by scalar -> 0.114):');
      disp(gamma .* original(:,:,3));
      disp(char(10)); % New line
   else
      disp('Image size too large to display matrix operations. Please use an image that is 16x16 pixels or less in size. ');
   endif

  image_gs = alpha .* original(:,:,1) + beta .* original(:,:,2) + gamma .* original(:,:,3);
end

