function out_img = matrix_arithmetic(img1, img2, op)
  % Showcase a Matrix operation between two images of the same size
  % -> Matrix Multiplication / Matrix Division / Matrix Addition / Matrix Subtraction

  % Check if Image 1 is a 16x16 matrix (pixels) or less
  % NOTE: Image 1 and Image 2 will be of the same size so check either of them
  [height, width] = size(img1);
  % NOTE: Width will be 3x the width of the image as the number of pixels (eg. 16) will be multiplied by the 3 RGB colour channels (eg. 16*3 = 48)

  if height <= 16 && (width / 3) <= 16
    disp(char(10)); % New line
    disp('Image 1:');
    disp(img1);
    disp(char(10)); % New line
    disp('Image 2:');
    disp(img2);
    disp(char(10)); % New line

    if isequal(op, '*') % Multiply images together
      disp('The following operation will be performed:');
      disp('Multiply Image 1 BY Image 2');
      disp(char(10)); % New line
      disp('Resulting matrix :');
      disp(img1 .* img2);
      disp(char(10)); % New line

      out_img = img1 .* img2;
    elseif isequal(op, '/') % Divide img1 by img2
      disp('The following operation will be performed:');
      disp('Divide Image 1 BY Image 2');
      disp(char(10)); % New line
      disp('Resulting matrix :');
      disp(img1 ./ img2);
      disp(char(10)); % New line

      out_img = img1 ./ img2;
    elseif isequal(op, '+') % Add images together
      disp('The following operation will be performed:');
      disp('Add Image 1 AND Image 2');
      disp(char(10)); % New line
      disp('Resulting matrix :');
      disp(img1 .+ img2);
      disp(char(10)); % New line

      out_img = img1 .+ img2;
    elseif isequal(op, '-') % Subtract img1 from img2
      disp('The following operation will be performed:');
      disp('Subtract Image 2 FROM Image 1');
      disp(char(10)); % New line
      disp('Resulting matrix :');
      disp(img1 .- img2);
      disp(char(10)); % New line

      out_img = img1 .- img2;
    else
      disp("No operator selected");
      out_img = zeros(size(img1));
    endif
  else
    % Simply calculate and return output image if image > 16x16 pixels

    disp('Image size too large to display matrix operations. Please use an image that is 16x16 pixels or less in size. ');

    if isequal(op, '*') % Multiply images together
      out_img = img1 .* img2;
    elseif isequal(op, '/') % Divide img1 by img2
      out_img = img1 ./ img2;
    elseif isequal(op, '+') % Add images together
      out_img = img1 .+ img2;
    elseif isequal(op, '-') % Subtract img1 from img2
      out_img = img1 .- img2;
    else
      disp("No operator selected");
      out_img = zeros(size(img1));
    endif
  endif
end
