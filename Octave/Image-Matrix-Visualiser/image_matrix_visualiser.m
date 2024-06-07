function image_matrix_visualiser()
  % Create a figure
  f = figure('position', [300 100 800 500], 'Resize', 'off', 'Name', 'Image Matrix Visualiser', 'NumberTitle', 'off');

  % Store the size of the figure
  figSize = get(f, 'Position');

  mainMenu();

  function mainMenu(hObject, eventdata)
    clf; % Clear the figure (remove previous UI elements if applicable)

    title_vector = [(figSize(3) - 800) / 2, figSize(4) / 1.35, 800, 30]; % [x, y, width, height]
    title = uicontrol(f, 'style', 'text', ...
                      'string', 'Image Matrix Visualiser', ...
                      'position', title_vector, ...
                      'fontsize', 18, ...
                      'fontweight', 'bold');

    name_vector = [(figSize(3) - 800) / 2, (figSize(4) / 1.35) - 30, 800, 30]; % [x, y, width, height]
    name = uicontrol(f, 'style', 'text', ...
                     'string', 'by Vlad Florea', ...
                     'position', name_vector, ...
                     'fontsize', 12, ...
                     'foregroundcolor', 'red');

    matrix_arithmetic_button_vector = [figSize(3) / 4, (figSize(4) - 30) / 2, 200, 30]; % [x, y, width, height]
    matrix_arithmetic_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Matrix Arithmetic (2 Images)', ...
                                         'position', matrix_arithmetic_button_vector, ...
                                         'callback', @matrixArithmetic);

    matrix_manipulation_button_vector = [figSize(3) / 4 + figSize(3) / 4, (figSize(4) - 30) / 2, 200, 30]; % [x, y, width, height]
    matrix_manipulation_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Matrix Manipulation (1 Image)', ...
                                           'position', matrix_manipulation_button_vector, ...
                                           'callback', @matrixManipulation);
  endfunction


  % Callback function for 'Matrix Arithmetic' button
  function matrixArithmetic(hObject, eventdata)
    % Matrix Arithmetic --- Upload 2 Images

    clf; % Clear the figure (remove MA / MM buttons)

    % Back button to return to main menu
    back_button_vector = [25, figSize(4) - 50, 120, 30]; % [x, y, width, height]
    back_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Back', ...
                            'position', back_button_vector, ...
                            'callback', @mainMenu, ...
                            'BackgroundColor', 'red');

    title_vector = [(figSize(3) - 800) / 2, figSize(4) / 1.35, 800, 30]; % [x, y, width, height]
    title = uicontrol(f, 'style', 'text', ...
                      'string', 'Matrix Arithmetic', ...
                      'position', title_vector, ...
                      'fontsize', 18, ...
                      'fontweight', 'bold');

    note_1_vector = [(figSize(3) - 800) / 2, (figSize(4) / 1.35) - 30, 800, 30]; % [x, y, width, height]
    note_1 = uicontrol(f, 'style', 'text', ...
                       'string', '(1) To visualise Image Matrix operations, select images that are 16x16 pixels or less in size', ...
                       'position', note_1_vector, ...
                       'fontsize', 12);

    note_2_vector = [(figSize(3) - 800) / 2, (figSize(4) / 1.35) - 60, 800, 30]; % [x, y, width, height]
    note_2 = uicontrol(f, 'style', 'text', ...
                       'string', '(2) Both images must be of the same size (same pixel height/width)', ...
                       'position', note_2_vector, ...
                       'fontsize', 12);

    upload_image_1_vector = [(figSize(3) - 200) / 2, (figSize(4) - 30) / 2, 200, 30]; % [x, y, width, height]
    upload_image_1_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Upload Image 1', ...
                            'position', upload_image_1_vector, ...
                            'callback', @uploadImage1);

  endfunction

  % Callback function for 'Upload Image 1' button
  function uploadImage1(hObject, eventdata)
    % Open a file selection dialog for image files
    [fname, fpath] = uigetfile({'*.jpg;*.png;*.gif', 'Image Files'}, 'Select FIRST image file');

    % Check if a file was selected (file name is NOT empty)
    if ~isempty(fname)
      % Construct the full file path
      fullPath = fullfile(fpath, fname);

      % Store the image file from the full file path into a variable
      img1 = imread(fullPath);

      clf; % Clear 'Upload Image 1' button

      % Back button to return back to image upload screen
      back_button_vector = [25, figSize(4) - 50, 120, 30]; % [x, y, width, height]
      back_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Back', ...
                             'position', back_button_vector, ...
                             'callback', @matrixArithmetic, ...
                             'BackgroundColor', 'red');

      % Display selected Image 1's file name
      image_1_selected_file_vector = [(figSize(3) - 500) / 2, figSize(4) / 1.5, 500, 30]; % [x, y, width, height]
      image_1_selected_file = uicontrol(f, 'style', 'text', ...
                                        'string', ["Image 1: " fname], ...
                                        'position', image_1_selected_file_vector, ...
                                        'fontsize', 12, ...
                                        'fontweight', 'bold', ...
                                        'backgroundcolor', 'green');

      upload_image_2_vector = [(figSize(3) - 200) / 2, (figSize(4) - 30) / 2, 200, 30]; % [x, y, width, height]
      upload_image_2_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Upload Image 2', ...
                                        'position', upload_image_2_vector, ...
                                        'callback', @(hObject, eventdata) uploadImage2(hObject, eventdata, img1));
    else
      disp('No file selected.');
    end
  endfunction

  % Callback function for 'Upload Image 2' button
  function uploadImage2(hObject, eventdata, img1)
    % Open a file selection dialog for image files
    [fname, fpath] = uigetfile({'*.jpg;*.png;*.gif', 'Image Files'}, 'Select SECOND image file');

    % Check if a file was selected (file name is NOT empty)
    if ~isempty(fname)
      % Construct the full file path
      fullPath = fullfile(fpath, fname);

      % Store the image file from the full file path into a variable
      img2 = imread(fullPath);

      % Display selected Image 2's file name
      image_2_selected_file_vector = [(figSize(3) - 500) / 2, (figSize(4) / 1.5) - 40, 500, 30]; % [x, y, width, height]
      image_2_selected_file = uicontrol(f, 'style', 'text', ...
                                        'string', ["Image 2: " fname], ...
                                        'position', image_2_selected_file_vector, ...
                                        'fontsize', 12, ...
                                        'fontweight', 'bold', ...
                                        'backgroundcolor', 'green');

      % Start button
      start_button_vector = [(figSize(3) - 200) / 2, figSize(4) / 3, 200, 30]; % [x, y, width, height]
      start_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Start', ...
                               'position', start_button_vector, ...
                               'callback', @(hObject, eventdata) doubleImageArithmetic(hObject, eventdata, img1, img2), ...
                               'BackgroundColor', 'green');
    else
      disp('No file selected.');
    end
  endfunction

  function doubleImageArithmetic(hObject, eventdata, img1, img2)

    clf; % Clear all previous UI elements

    % Back button to return back to image upload screen
    back_button_vector = [25, figSize(4) - 50, 120, 30]; % [x, y, width, height]
    back_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Back', ...
                            'position', back_button_vector, ...
                            'callback', @matrixArithmetic, ...
                            'BackgroundColor', 'red');

    if ~exist("img1", "var") == 1 || ~exist("img2", "var") == 1

      % Display an error message to user (Must select 2 images)
      select_images_error_vector = [(figSize(3) - 300) / 2, (figSize(4) - 30) / 2, 300, 30]; % [x, y, width, height]
      select_images_error = uicontrol(f, 'style', 'text', ...
                            'string', 'Error: Please select TWO images', ...
                            'position', select_images_error_vector, ...
                            'fontsize', 12, ...
                            'fontweight', 'bold', ...
                            'backgroundcolor', 'red');

    elseif ~isequal(size(img1), size(img2))
        % Display an error message to user (Images must be the same size)
        image_size_error_vector = [(figSize(3) - 400) / 2, (figSize(4) - 30) / 2, 400, 30]; % [x, y, width, height]
        image_size_error = uicontrol(f, 'style', 'text', ...
                                     'string', 'Error: Images must be of the same size', ...
                                     'position', image_size_error_vector, ...
                                     'fontsize', 12, ...
                                     'fontweight', 'bold', ...
                                     'backgroundcolor', 'red');

    else
      % Main matrix arithmetic logic

      subplot(1, 3, 1), imshow(img1);
      subplot(1, 3, 2), imshow(img2);

      calculation_operator = '*'; % Initially multiply

      displayed_operator_vector = [(figSize(3) + 60) / 3, (figSize(4) - 25) / 2, 25, 30]; % Middle of the FIRST two images
      displayed_operator = uicontrol(f, 'style', 'text', ...
                                     'string', '*', ...
                                     'position', displayed_operator_vector, ...
                                     'fontsize', 12, ...
                                     'fontweight', 'bold');

      equals_operator_vector = [(figSize(3) - 25) / 1.5, (figSize(4) - 25) / 2, 25, 30]; % Middle of the LAST two images
      equals_operator = uicontrol(f, 'style', 'text', ...
                                  'string', '=', ...
                                  'position', equals_operator_vector, ...
                                  'fontsize', 12, ...
                                  'fontweight', 'bold');

      % %%%%%%%%%%%%%%%%%%%% Six different options: %%%%%%%%%%%%%%%%%%%%

      %%%%% 1. Multiply
      multiply_button_vector = [10, figSize(4) / 6, 120, 30]; % [x, y, width, height]
      multiply_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Multiply', ...
                                  'position', multiply_button_vector, ...
                                  'callback', @setMultiply);

      function setMultiply(hObject, eventdata)
        set(displayed_operator, 'string', '*'); % Set displayed operator text to multiply symbol
        calculation_operator = '*';
      endfunction

      %%%%% 2. Divide
      divide_button_vector = [140, figSize(4) / 6, 120, 30]; % [x, y, width, height]
      divide_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Divide', ...
                                  'position', divide_button_vector, ...
                                  'callback', @setDivide);

     function setDivide(hObject, eventdata)
        set(displayed_operator, 'string', '/'); % Set displayed operator text to divide symbol
        calculation_operator = '/';
     endfunction

     %%%%% 3. Add
     add_button_vector = [270, figSize(4) / 6, 120, 30]; % [x, y, width, height]
     add_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Add', ...
                                  'position', add_button_vector, ...
                                  'callback', @setAdd);

     function setAdd(hObject, eventdata)
       set(displayed_operator, 'string', '+'); % Set displayed operator text to add symbol
       calculation_operator = '+';
     endfunction

     %%%%% 4. Subtract
     subtract_button_vector = [400, figSize(4) / 6, 120, 30]; % [x, y, width, height]
     subtract_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Subtract', ...
                                  'position', subtract_button_vector, ...
                                  'callback', @setSubtract);

     function setSubtract(hObject, eventdata)
       set(displayed_operator, 'string', '-'); % Set displayed operator text to subtract symbol
       calculation_operator = '-';
     endfunction

     %%%%% 5. Swap Images
     swap_button_vector = [530, figSize(4) / 6, 120, 30]; % [x, y, width, height]
     swap_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Swap', ...
                             'position', swap_button_vector, ...
                             'callback', @swap, ...
                             'BackgroundColor', 'yellow');

     function swap(hObject, eventdata)
      temp = img1;
      img1 = img2;
      img2 = temp;

      subplot(1, 3, 1), imshow(img1);
      subplot(1, 3, 2), imshow(img2);
     endfunction

     %%%%% 6. Calculate
     calculate_button_vector = [660, figSize(4) / 6, 130, 30]; % [x, y, width, height]
     calculate_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Calculate', ...
                                  'position', calculate_button_vector, ...
                                  'callback', @calculate, ...
                                  'BackgroundColor', 'green');

     function calculate(hObject, eventdata)
      out_img = matrix_arithmetic(img1, img2, calculation_operator);
      subplot(1, 3, 3), imshow(out_img);
     endfunction
    endif
   endfunction

  function matrixManipulation(hObject, eventdata)
    % Matrix Manipulation --- Upload 1 Image

    clf; % Clear the figure (remove MA / MM buttons)

    % Back button to return to main menu
    back_button_vector = [25, figSize(4) - 50, 120, 30]; % [x, y, width, height]
    back_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Back', ...
                            'position', back_button_vector, ...
                            'callback', @mainMenu, ...
                            'BackgroundColor', 'red');

    title_vector = [(figSize(3) - 800) / 2, figSize(4) / 1.35, 800, 30]; % [x, y, width, height]
    title = uicontrol(f, 'style', 'text', ...
                      'string', 'Matrix Manipulation', ...
                      'position', title_vector, ...
                      'fontsize', 18, ...
                      'fontweight', 'bold');

    note_vector = [(figSize(3) - 800) / 2, (figSize(4) / 1.35) - 30, 800, 30]; % [x, y, width, height]
    note = uicontrol(f, 'style', 'text', ...
                     'string', '(1) To visualise Image Matrix operations, select an image that is 16x16 pixels or less in size', ...
                     'position', note_vector, ...
                     'fontsize', 12);

    % Create an 'Upload Image' button in the center of the figure
    upload_button_vector = [(figSize(3) - 200) / 2, (figSize(4) - 30) / 2, 200, 30]; % [x, y, width, height]
    upload_image_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Upload Image', ...
                                    'position', upload_button_vector, ...
                                    'callback', @manipulateSingleImage);
  endfunction

  % Callback function for 'Upload Image' button
  function manipulateSingleImage(hObject, eventdata)
    % Open a file selection dialog for image files
    [fname, fpath] = uigetfile({'*.jpg;*.png;*.gif', 'Image Files'}, 'Select an image file');

    % Check if a file was selected (file name is NOT empty)
    if ~isempty(fname)
      % Construct the full file path
      fullPath = fullfile(fpath, fname);

      % Store the image file from the full file path into a variable
      img = imread(fullPath);

      clf; % Clear the figure (remove previous buttons)

      % Back button to return to main menu
      back_button_vector = [25, figSize(4) - 50, 120, 30]; % [x, y, width, height]
      back_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Back', ...
                              'position', back_button_vector, ...
                              'callback', @matrixManipulation, ...
                              'BackgroundColor', 'red');

      original = img;
      imshow(img); % Show the original image at first
      title(['Selected Image: ', fname], 'Interpreter', 'none'); % Display selected image file name

      %%%%%%%%%%%%%%%%%%%% Six different filters: %%%%%%%%%%%%%%%%%%%%

      %%%%% 0. Original
      original_button_vector = [(figSize(3) - 90) / 2, 10, 120, 30]; % [x, y, width, height]
      original_image_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Original', ...
                                        'position', original_button_vector, ...
                                        'callback', @originalImage, ...
                                        'BackgroundColor', 'green');

      function originalImage(hObject, eventdata)
        imshow(original);
      endfunction

      %%%%% 1. Grayscale
      grayscale_button_vector = [50, 150, 120, 30]; % [x, y, width, height]
      grayscale_image_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Grayscale', ...
                                         'position', grayscale_button_vector, ...
                                         'callback', @grayscaleImage);

      function grayscaleImage(hObject, eventdata)
        grayscale = grayscale(img);
        imshow(grayscale);
      endfunction

      %%%%% 2. Inverted
      inverted_button_vector = [50, 200, 120, 30]; % [x, y, width, height]
      inverted_image_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Inverted', ...
                                        'position', inverted_button_vector, ...
                                        'callback', @invertedImage);

      function invertedImage(hObject, eventdata)
        inverted = invert(img);
        imshow(inverted);
      endfunction

      %%%%% 3. Sepia
      sepia_button_vector = [50, 250, 120, 30]; % [x, y, width, height]
      sepia_image_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Sepia', ...
                                     'position', sepia_button_vector, ...
                                     'callback', @sepiaImage);

      function sepiaImage(hObject, eventdata)
        sepia = sepia(img);
        imshow(sepia);
      endfunction

      %%%%% 4. Red Channel
      red_channel_button_vector = [figSize(3) - 150, 150, 120, 30]; % [x, y, width, height]
      red_channel_image_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Red Channel', ...
                                           'position', red_channel_button_vector, ...
                                           'callback', @redImage);

      function redImage(hObject, eventdata)
        red_img = red_channel(img);
        imshow(red_img);
      endfunction

      %%%%% 5. Green Channel
      green_channel_button_vector = [figSize(3) - 150, 200, 120, 30]; % [x, y, width, height]
      green_channel_image_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Green Channel', ...
                                             'position', green_channel_button_vector, ...
                                             'callback', @greenImage);

      function greenImage(hObject, eventdata)
        green_img = green_channel(img);
        imshow(green_img);
      endfunction

      %%%%% 6. Blue Channel
      blue_channel_button_vector = [figSize(3) - 150, 250, 120, 30]; % [x, y, width, height]
      blue_channel_image_button = uicontrol(f, 'style', 'pushbutton', 'string', 'Blue Channel', ...
                                            'position', blue_channel_button_vector, ...
                                            'callback', @blueImage);

      function blueImage(hObject, eventdata)
        blue_img = blue_channel(img);
        imshow(blue_img);
      endfunction
    else
      disp('No file selected.');
    end
  endfunction

end

