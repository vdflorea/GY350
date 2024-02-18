#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>

typedef struct {
    char description[100];
    int n, s, e, w, in, out;
} location;

typedef struct {
    char description[100];
    char name[100];
    int associatedLoc;
    bool isInInventory;
} object;

location locations[40];
object objects[40];

int numLocations = 0;
int numObjects = 0;
char locationsFile[] = "C:\\Users\\Vlad\\Desktop\\adventure_locations.txt";
char objectsFile[] = "C:\\Users\\Vlad\\Desktop\\adventure_objects.txt";

bool readLocations();
bool readObjects();
FILE* openFileForReading(char*);
void startGame();
void lowercaseInput(char []);
bool doesItemExist(char []);
bool isItemInInventory(char []);
int findItemIndex(char item[]);

int main() {
    if (readLocations() && readObjects()) {
        printf("Successfully read %d locations from file\n", numLocations);
        startGame();
    }
}

FILE* openFileForReading(char* filename) {
    FILE* file_ptr;
    fopen_s(&file_ptr, filename, "r");

    if (file_ptr == NULL) {
        printf("Could not open %s\n", filename);
    }

    return file_ptr;
}

bool readLocations() {
    FILE* file_ptr = openFileForReading(locationsFile);
    if (file_ptr == NULL) {
        return false;
    }

    int readHeaderLines = 0;
    char line[200];
    while (fgets(line, 99, file_ptr) != NULL) {
        if (readHeaderLines<2)
            // Excludes the top two header lines of the file
            readHeaderLines++;
        else {
            numLocations++;
            location l;
            int locNum;
            sscanf(line, "%d\t%d\t%d\t%d\t%d\t%d\t%d\t%[^\t]\n", &locNum, &l.n, &l.s, &l.e, &l.w, &l.in, &l.out, l.description); // Parse each line of txt file
            locations[numLocations] = l; // Assign the contents of the variable l into the locations array at the specific location position (Starting at location 1)
        }
    }

    fclose(file_ptr);
    return true;
}

bool readObjects() {
    FILE* file_ptr = openFileForReading(objectsFile);
    if (file_ptr == NULL) {
        return false;
    }

    int readHeaderLines = 0;
    char line[200];
    while (fgets(line, 99, file_ptr) != NULL) {
        if (readHeaderLines<2)
            // Excludes the top two header lines of the file
            readHeaderLines++;
        else {
            object o;
            o.isInInventory = false;
            int locNum;

            sscanf(line, "%[^\t]\t%d\t%[^\n]\n", o.name, &locNum, o.description);

            o.associatedLoc = locNum;
            //printf("%s\t%s\t%d\t%d", o.name, o.description, o.associatedLoc, o.isInInventory);
            objects[numObjects] = o; // Assign the contents of the variable o into the objects array at the specific object position (Starting at object 0)
            numObjects++;
        }
    }

    fclose(file_ptr);
    return true;

}

void startGame() {
    int idx = 1;
    char userInput[20] = "";
    char itemInput[20] = "";
    int prevIdx;
    bool objectsPresent = false;
    printf("\nWelcome to Galway Adventure! Type 'help' for help.\n");

    while (strcmp(userInput, "quit") != 0) {
        prevIdx = idx; // Required to swap the index back if the current index is 0 (location 0 does not exist)
        printf("\n%s", locations[idx].description);
        printf("Objects here:\t\t");

        for (int i = 0; i < numObjects; i++) {
            if (objects[i].associatedLoc == idx) {
                printf("%s\t\t", objects[i].name);
                objectsPresent = true;
            }
        }

        if (!objectsPresent) {
            printf("None");
        }

        printf("\n>");
        gets(userInput);
        lowercaseInput(userInput);

        if (strcmp(userInput, "n") == 0) {
            // If the player types the command "n", a new index is assigned corresponding to the new location which is accessed through "n"
            idx = locations[idx].n;
        } else if (strcmp(userInput, "s") == 0) {
            idx = locations[idx].s;
        } else if (strcmp(userInput, "e") == 0) {
            idx = locations[idx].e;
        } else if (strcmp(userInput, "w") == 0) {
            idx = locations[idx].w;
        } else if (strcmp(userInput, "in") == 0) {
            idx = locations[idx].in;
        } else if (strcmp(userInput, "out") == 0) {
            idx = locations[idx].out;
        } else if (strcmp(userInput, "look") == 0) {
            // Do nothing
        }


        /***************ITEM COMMANDS***************/
        /**********TAKE ITEM**********/
        else if (strcmp(userInput, "take") == 0) {

            if (objectsPresent) {
                printf("\nEnter the item you would like to take:\n>");
                gets(itemInput);

                if (!isItemInInventory(itemInput) && doesItemExist(itemInput)) {
                    // If the item is in the player's inventory already AND the item 'name' exists

                    printf("\n%s taken!\n", itemInput);

                    objects[findItemIndex(itemInput)].associatedLoc = 0; // No location associated with the item anymore
                    objects[findItemIndex(itemInput)].isInInventory = true; // The item is now in the player's inventory
                } else if (!doesItemExist(itemInput)) {
                    printf("\nItem does not exist!\n");
                } else {
                    printf("\nItem already in inventory!\n");
                }
            }
            else {
                printf("\nNo objects present at this location!\n");
            }


        /**********DROP ITEM**********/
        } else if (strcmp(userInput, "drop") == 0) {

            printf("\nEnter the item you would like to drop:\n>");
            gets(itemInput);

            if (isItemInInventory(itemInput)) {
                // If the item is in the inventory of the player

                printf("\n%s dropped!\n", itemInput);
                objects[findItemIndex(itemInput)].associatedLoc = idx; // New location associated with the item
                objects[findItemIndex(itemInput)].isInInventory = false; // The item is no longer in the player's inventory
            } else {
                printf("\nNo such item in inventory!\n");
            }


        /**********INVENTORY VIEW**********/
        } else if (strcmp(userInput, "inventory") == 0) {

            bool itemFound = false; // Local variable for this specific else if statement
            printf("\nItems in inventory:\n");

            for (int i = 0; i < numObjects; i++) {
                if (objects[i].isInInventory == true) {
                    // If the item is in the player's inventory, print out that item

                    printf("%s\n", objects[i].name);
                    itemFound = true;
                }
            }
            if (!itemFound) {
                printf("\nNo items in inventory!\n");
            }


        /**********EXAMINE ITEM**********/
        } else if (strcmp(userInput, "examine") == 0) {

            bool itemFound = false; // Local variable for this specific else if statement
            printf("\nWhat item would you like to examine?\n>");
            gets(itemInput);

            for (int i = 0; i < numObjects && itemFound == false; i++) {
                if (strcmp(objects[i].name, itemInput) == 0 && objects[i].isInInventory == true) {
                    // For this if statement to be true --> The name must match object[i] AND that object must be in the player's inventory
                    // Very similar to isItemInInventory BUT tracks the exact item we are looking for (isItemInInventory is only a logic function)

                    printf("\n%s", objects[i].description);
                    itemFound = true;
                }
            }
            if (itemFound == false) {
                printf("\nNo such item in inventory!\n");
            }
            printf("\n");
        }


        else if (strcmp(userInput, "help") == 0) {
            printf("\nCommands:\n");
            printf("n, s, e, w, in, out, look, take, drop, inventory, examine, help, quit\n");
        } else if (strcmp(userInput, "quit") != 0){
            // Any other input other than the commands is ignored
            printf("\nInvalid Input!\n");
        }

        if (idx == 0) {
            // If idx == 0, it is assigned the previous index and thus, the same location is displayed as before
            printf("\nYou cannot go there!\n");
            idx = prevIdx;
        }

        objectsPresent = false; // Reset this for the next iteration of the loop
    }
    printf("\nBye!\n");
}

bool isItemInInventory(char item[]) {
    // Return 'true' IF name matches an object AND that object is in the player's inventory

    for (int i = 0; i < numObjects; i++) {
        if (objects[i].isInInventory == true && strcmp(objects[i].name, item) == 0) {
            return true;
        }
    }
    return false;
}

bool doesItemExist(char item[]) {
    // Return 'true' IF name matches an object

    for (int i = 0; i < numObjects; i++) {
        if (strcmp(objects[i].name, item) == 0) {
            return true;
        }
    }
    return false;
}

int findItemIndex(char item[]) {
    // Finds the index of an item in the objects array by its name

    for (int i = 0; i < numObjects; i++) {
        if (strcmp(objects[i].name, item) == 0) {
            return i;
        }
    }
}

void lowercaseInput(char input[]) {
    // Turns each character of the user's input into lowercase

    for (int i = 0; i < strlen(input); i++) {
        input[i] = tolower(input[i]);
    }
}

