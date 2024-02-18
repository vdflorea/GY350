
#include <stdio.h>

typedef struct{
    int day, month, year;
} date;

typedef struct{
    char name[50];
    int accountNumber;
    double balance;
    date lastTrans;
} customer;

void getCustomerName(customer* cptr);
void getAccountNumber(customer* cptr);
void getLastTransDate(customer* cptr);
void getBalance(customer* cptr);
void printCustomer(customer* cptr);

void main(){
    int i;
    customer customers[3];
    for (i = 0; i < 3; i++){
        getCustomerName(&customers[i]); // Sends the address of the customer (structure) details to the function as a pointer
        getAccountNumber(&customers[i]); // Sends the address of the customer (structure) details to the function as a pointer
        getLastTransDate(&customers[i]); // Sends the address of the customer (structure) details to the function as a pointer
        getBalance(&customers[i]); // Sends the address of the customer (structure) details to the function as a pointer
        printf("\n");
    }
    printf("\n\n%25s\t%13s\t%12s\t%s\n\n", "Name", "Account Number",
           "Balance", "Date of Last Transaction"); // Prints out the header line for customer details
    for (i = 0; i < 3; i++){
        printCustomer(&customers[i]); // Sends the address of the customer (structure) details to the function as a pointer
    }
}

void getCustomerName(customer* cptr) {
    printf("Enter Customer First Name:");
    scanf_s("%s", cptr->name);
    // Scans the user's input
    // Stores a string in the ith customer's name variable in the customer struct
}

void getAccountNumber(customer* cptr) {
    printf("Enter Customer Account Number (XXX):");
    scanf_s("%d", &(cptr->accountNumber));
    // Scans the user's input
    // Stores an integer in the ith customer's account number variable in the customer struct
}

void getLastTransDate(customer* cptr) {
    printf("Enter Last Transaction Date (dd/mm/yyyy):");
    scanf_s("%d/%d/%d", &(cptr->lastTrans.day), &(cptr->lastTrans.month), &(cptr->lastTrans.year));
    // Scans the user's input
    // Stores integers in the ith customer's day, month, & year variables in the date struct (accessed through customer struct)
}

void getBalance(customer* cptr) {
    printf("Enter Balance:");
    scanf_s("%lf", &(cptr->balance));
    // Scans the user's input
    // Stores a double in the ith customer's balance variable in the customer struct
}

void printCustomer(customer* cptr) {
    printf("%25s\t%13d\t%12lf\t%d/%d/%d\n\n", cptr->name, cptr->accountNumber, cptr->balance, cptr->lastTrans.day, cptr->lastTrans.month, cptr->lastTrans.year);
    // Prints out each customer's information in the same format as the header on line 35 in main()
}

