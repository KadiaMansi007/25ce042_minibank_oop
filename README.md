MINIBANK - PART B (OOPJ PRACTICALS 1-14)

Requirements
- JDK 17 or newer
- Command Prompt or PowerShell

Run in Command Prompt
1. Extract this ZIP file.
2. Open the extracted MiniBank_Part_B folder.
3. Click the folder address bar, type cmd and press Enter.
4. Check Java is installed:
   java -version
   javac -version
5. Compile all files:
   javac -d out src\MiniBank.java src\model\*.java src\model\annotation\*.java src\exception\*.java src\util\*.java src\service\*.java
6. Run the program:
   java -cp out MiniBank

Build a runnable JAR
1. Compile the files using step 5 above.
2. Create the JAR:
   jar --create --file minibank.jar --main-class MiniBank -C out .
3. Run it:
   java -jar minibank.jar

Menu
1 Open account
2 Deposit
3 Withdraw
4 Transfer
5 Statement
6 List accounts
7 Generate report
8 Batch deposits
9 Exit

The program creates data\accounts.dat, data\report.txt and logs\transactions.log when needed. Accounts are saved when you select Exit and loaded when you start the program again.

This is a student-level integrated implementation of the Part B MiniBank requirements in the supplied CEUE203 practical list. Use sample accounts only; it is coursework software, not a real banking system. Code comments have not been added.

Menu option 9 checks an account balance. Enter the account number to see the current balance.
