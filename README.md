# 📱 Spendify - Budget Tracker App

Spendify is a simple and intuitive budget tracking app built for Android using **Kotlin**, **RoomDB**, and **Jetpack components**. It helps users track daily expenses, categorize spending, and manage their personal budget effectively—all offline and without login.

---

## 🚀 Features

- 💸 Add and view expenses with amount, description, and category
- 📷 Attach photos to expense entries
- 🗂️ Create, edit, and delete custom categories
- 📋 View a list of all expenses in a clean UI
- ✏️ Edit or delete existing expenses
- 🗃️ Data is persisted locally using RoomDB

---

## 🛠️ Tech Stack

| Layer        | Tech                    |
|--------------|-------------------------|
| Language     | Kotlin                  |
| Database     | Room (SQLite wrapper)   |
| Architecture| MVVM (simplified)       |
| UI          | XML Layouts             |
| Image Input | Gallery (via Intent)    |

---

## ✅ How to Run

1. Clone this repo in Android Studio
2. Build the project
3. Run on an emulator or physical device (Android 5.0+)
4. Start adding categories and expenses!

---


## 📌 Notes

- No authentication or cloud storage: everything is stored locally.
- App does not require an internet connection.
- Images are stored as `Uri.toString()` pointing to local gallery images.

---


## 🔧 Future Improvements (Optional)

- Filter expenses by category or amount
- Export expenses as PDF/CSV
- Add charts for expense breakdown
- Support for Firebase (multi-device sync)

---


## 🧑‍💻 Author

Built by Preshen, Devesh, and Keyur as part of a learning project or assignment.

