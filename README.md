
# SecureDocs - Confidential Document Storage App

SecureDocs is an Android application that allows users to securely store confidential documents in the device's internal storage. These documents are protected with a PIN code, ensuring that only authorized users can access them. The app leverages Android's runtime permissions for file access, providing a secure way to handle sensitive files.

## Features

- **PIN Protection:** Store and access confidential documents securely with a PIN code.
- **Runtime Permissions:** Uses runtime permissions to handle file access, ensuring the app respects Android's permission model.
- **Secure Document Storage:** Documents are stored in the internal storage, ensuring they are not accessible by unauthorized apps or users.
- **File View & Delete:** Ability to view stored files and delete them securely.

## Prerequisites

- Android Studio
- Kotlin 1.4+ (or higher)
- Android SDK 30+ (or higher)
- Runtime permissions for storage access

## Getting Started

To get started with SecureDocs, follow these steps:

1. Clone the repository:
    ```bash
    git clone https://github.com/ervg0203/SecureDocsApplication.git
    ```

2. Open the project in Android Studio.

3. Make sure to grant necessary permissions when prompted. The app requires access to internal storage to store and manage documents.

4. Build and run the app on an emulator or a physical device.

## App Structure

- **MainActivity.kt:** Entry point of the app where users are prompted to enter their PIN to access stored documents.
- **FileActivity.kt:** Displays a list of stored documents and provides options to view or delete them.
- **PinManager.kt:** Handles PIN verification, storage, and management logic.
- **activity_main.xml:** Layout for the main activity where users input their PIN.
- **activity_file.xml:** Layout for the file management activity.

## How It Works

1. When the user opens the app, they are prompted to set a PIN.
2. Documents are securely saved in the internal storage.
3. Upon reopening the app, the user must enter the correct PIN to view or manage stored files.
4. Runtime permissions are requested to grant the app access to the device's storage.
5. Users can view or delete stored files from the app, with security ensuring that only authorized users can interact with the files.

## Contributing

Contributions are welcome! If you find any bugs or have ideas for improvement, please feel free to open an issue or submit a pull request.

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes.
4. Commit and push your changes.
5. Open a pull request.


## images 
![image](https://github.com/user-attachments/assets/a171023d-1999-4bc6-aedc-0bd00c6ed934)
![image](https://github.com/user-attachments/assets/a3b7560f-45e3-4b21-9ef0-a2d02e3c9283)
![image](https://github.com/user-attachments/assets/ae6785c3-e0ec-4a23-97cf-0b99f9df302e)
![image](https://github.com/user-attachments/assets/a5ee9607-569a-4062-824a-020c22cc6263)
![image](https://github.com/user-attachments/assets/bda2fdb1-e24f-4ac4-9cab-aee0f1b9bced)
![image](https://github.com/user-attachments/assets/8bae44b3-15cb-45bb-8281-f439c001ff81)
![image](https://github.com/user-attachments/assets/0faa9dba-3229-43c0-b36f-c8045496e620)





