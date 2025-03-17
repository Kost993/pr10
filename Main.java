import java.util.Scanner;

class User {
    String username;
    String password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}

class InvalidUsernameException extends Exception {
    public InvalidUsernameException(String message) {
        super(message);
    }
}

class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}

class MaxUsersExceededException extends Exception {
    public MaxUsersExceededException(String message) {
        super(message);
    }
}

class UserManager {
    private static final int MAX_USERS = 15;
    private final User[] users = new User[MAX_USERS];
    private final String[] forbiddenPasswords = {"admin", "pass", "password", "qwerty", "ytrewq"};

    public void registerUser(String username, String password) throws InvalidUsernameException, InvalidPasswordException, MaxUsersExceededException {
        if (!isValidUsername(username)) throw new InvalidUsernameException("Недійсне ім'я користувача.");
        if (!isValidPassword(password)) throw new InvalidPasswordException("Недійсний пароль.");
        if (findUser(username) != -1) throw new InvalidUsernameException("Користувач вже існує.");


        for (int i = 0; i < MAX_USERS; i++) {
            if (users[i] == null) {
                users[i] = new User(username, password);
                return;
            }
        }
        throw new MaxUsersExceededException("Досягнуто максимальну кількість користувачів.");
    }

    public void removeUser(String username) throws UserNotFoundException {
        int index = findUser(username);
        if (index == -1) throw new UserNotFoundException("Користувача не знайдено.");
        users[index] = null;
    }

    public void authenticate(String username, String password) throws UserNotFoundException, InvalidPasswordException {
        int index = findUser(username);
        if (index == -1) throw new UserNotFoundException("Користувача не знайдено.");
        if (!users[index].password.equals(password)) throw new InvalidPasswordException("Неправильний пароль.");
    }

    private int findUser(String username) {
        for (int i = 0; i < MAX_USERS; i++) {
            if (users[i] != null && users[i].username.equals(username)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isValidUsername(String username) {
        if (username.length() < 5) return false;
        for (char c : username.toCharArray()) {
            if (c == ' ') return false;
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 10) return false;
        boolean hasSpecial = false;
        int digitCount = 0;

        for (char c : password.toCharArray()) {
            if (Character.isWhitespace(c)) return false;
            if (Character.isDigit(c)) digitCount++;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        if (!hasSpecial || digitCount < 3) return false;
        for (String forbidden : forbiddenPasswords) {
            if (password.toLowerCase().contains(forbidden)) return false;
        }
        return true;
    }
}

class AuthSystem {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UserManager userManager = new UserManager();

        while (true) {
            System.out.println("Меню:");
            System.out.println("1 - Додати користувача");
            System.out.println("2 - Видалити користувача");
            System.out.println("3 - Аутентифікація користувача");
            System.out.println("4 - Вийти");
            System.out.print("Виберіть опцію: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        userManager.registerUser(getInput("Ім'я: "), getInput("Пароль: "));
                        System.out.println("Користувач доданий.");
                        break;
                    case 2:
                        userManager.removeUser(getInput("Ім'я: "));
                        System.out.println("Користувач видалений.");
                        break;
                    case 3:
                        userManager.authenticate(getInput("Ім'я: "), getInput("Пароль: "));
                        System.out.println("Користувача аутентифіковано.");
                        break;
                    case 4:
                        System.out.println("Вихід...");
                        return;
                    default:
                        System.out.println("Невірний вибір.");
                }
            } catch (UserNotFoundException | InvalidUsernameException | InvalidPasswordException | MaxUsersExceededException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }

    private static String getInput(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}
