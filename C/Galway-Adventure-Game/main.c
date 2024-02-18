#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>

typedef struct {
    char description[100];
    int n, s, e, w, in, out;
} location;

location locations[40];
int numLocations = 0;
char locationsFile[] = "C:\\Users\\Vlad\\Desktop\\adventure_locations.txt";

bool readLocations();
FILE* openFileForReading(char*);
void startGame();
void lowercaseInput(char []);

int main() {
    if (readLocations()) {
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

    numLocations = 0;
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

    return true;
}

void startGame() {
    int idx = 1;
    char userInput[20] = "";
    int prevIdx;
    printf("\nWelcome to Galway Adventure! Type 'help' for help.\n");

    while (strcmp(userInput, "quit") != 0) {
        prevIdx = idx; // Required to swap the index back if the current index is 0 (location 0 does not exist)
        printf("\n%s", locations[idx].description);
        printf(">");
        gets(userInput);
        lowercaseInput(userInput);

        if (strcmp(userInput, "n") == 0) {
            // If the user types the command "n", a new index is assigned corresponding to the new location which is accessed through "n"
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
        } else if (strcmp(userInput, "help") == 0) {
            printf("\nCommands:\n");
            printf("n, s, e, w, in, out, look, help, quit\n");
        } else if (strcmp(userInput, "quit") != 0){
            // Any other input other than the commands is ignored
            printf("\nInvalid Input!\n");
        }

        if (idx == 0) {
            // If idx == 0, it is assigned the previous index and thus, the same location is displayed as before
            printf("\nYou cannot go there!\n");
            idx = prevIdx;
        }
    }
    printf("\nBye!\n");
}

void lowercaseInput(char input[]) {
    // Turns each character of the user's input into lowercase
    for (int i = 0; i < strlen(input); i++) {
        input[i] = tolower(input[i]);
    }
}
