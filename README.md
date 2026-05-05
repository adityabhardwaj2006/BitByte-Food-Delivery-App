# 🍔 BitByte - Food Delivery App

![Platform](https://img.shields.io/badge/Platform-Android-green)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-orange)
![Firebase](https://img.shields.io/badge/Backend-Firebase-yellow)
![License](https://img.shields.io/badge/License-MIT-red)

BitByte is a modern Food Delivery Android application built using **Jetpack Compose** and **Clean Architecture**, focused on scalable architecture, smooth user experience, and production-level Android development practices.

---

# 🚀 About This Project

This project is built to demonstrate my understanding of:

- Scalable Android architecture
- Modern UI development using Jetpack Compose
- Managing complex data flow between API, local storage, and cloud
- Real-world app features like authentication, cart, and payments
- Offline-first Android application development
- Dependency Injection and modular architecture

---

# ✨ Features

- 🌱 Veg / Non-Veg food filter
- 🛒 Cart management using Room Database
- 💳 Razorpay payment integration (Test Mode)
- 🔐 Firebase Authentication
- ☁️ Firestore for storing order history
- ⚙️ DataStore for user preferences (Address, UID, Veg Mode)
- 📡 API integration using Retrofit (JSONBin)
- 🎨 Smooth UI using Jetpack Compose & Lottie animations
- 📦 Offline support with local caching
- 🔄 Reactive state management using Flow & ViewModel
- 📱 Modern Material Design UI

---

# 🏗️ Architecture

- Clean Architecture + MVVM
- Separation of Data, Domain, and Presentation layers
- Repository Pattern
- Dependency Injection using Hilt
- Scalable and maintainable code structure

---

# 🛠️ Tech Stack

| Category | Technology |
|----------|-------------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM + Clean Architecture |
| Dependency Injection | Hilt |
| Networking | Retrofit + Gson |
| Local Storage | Room Database |
| Preferences | DataStore |
| Backend | Firebase Auth + Firestore |
| Payments | Razorpay |
| Image Loading | Coil |
| Animations | Lottie |
| Navigation | Navigation Compose |
| Async Programming | Coroutines + Flow |

---

# 📱 Application Flow

```text
User → Compose UI → ViewModel → UseCases → Repository → API / Room / Firebase
```

---

# 📂 Project Structure

```text
app/
│
├── data/
│   ├── remote/
│   ├── local/
│   ├── repository/
│   └── dto/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
│
├── presentation/
│   ├── screens/
│   ├── components/
│   ├── navigation/
│   └── viewmodel/
│
├── di/
│
└── utils/
```

---

# 📸 Screenshots
<p align="center">
  <img src="https://github.com/user-attachments/assets/7ab4629c-710d-46e0-b8ae-488082886395" width="230"/>

  <img src="https://github.com/user-attachments/assets/0529ecc5-8358-43a3-aac1-66b7fd836cf5" width="230"/>

  <img src="https://github.com/user-attachments/assets/a69122c9-f51d-4446-87ad-4d61351e8aa3" width="230"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/5eb444a6-4740-455d-8412-83a3c117100d" width="230"/>

  <img src="https://github.com/user-attachments/assets/03d86b7b-4178-48c8-bc76-4b8834c598e8" width="230"/>
</p>

---

# 🔥 Modern Android Concepts Used

- Jetpack Compose UI
- State Management
- MVVM Architecture
- Clean Architecture
- Repository Pattern
- Coroutines & Flow
- Offline Caching
- Firebase Integration
- Dependency Injection
- Payment Gateway Integration
- Navigation Component
- Modular Code Structure

---

# 🧠 Key Learnings

- Building scalable Android applications
- Managing complex UI states with Flow
- Implementing offline-first architecture
- Firebase Authentication & Firestore integration
- Payment gateway integration using Razorpay
- Optimizing Compose UI performance
- Writing maintainable and modular Android code

---

# ⚙️ Setup Instructions

## 1️⃣ Clone Repository

```bash
git clone https://github.com/your-username/BitByte.git
```

---

## 2️⃣ Open in Android Studio

- Open Android Studio
- Select "Open Project"
- Choose cloned repository

---

## 3️⃣ Add Firebase Configuration

Add your:

```text
google-services.json
```

inside:

```text
app/
```

---

## 4️⃣ Sync Gradle

Allow Android Studio to sync all dependencies.

---

## 5️⃣ Run the App

Connect:
- Physical Android Device OR
- Android Emulator

Then click ▶ Run.

---

# 🚀 Future Improvements

- 🔔 Push Notifications
- 📍 Live Order Tracking
- 🌙 Dark Mode
- 🤖 AI-based Recommendations
- 📊 Analytics Dashboard
- 🧾 Order Invoice Generation
- ⭐ Restaurant Ratings & Reviews
- ⚡ Pagination & Performance Optimization

---

# 👨‍💻 Developer

## Aditya Bhardwaj

- GitHub: [@adityabhardwaj2006](https://github.com/adityabhardwaj2006)

---

# ⭐ Support

If you liked this project:

- Give it a ⭐ on GitHub
- Fork the repository
- Share with developers

---

# 📄 License

This project is licensed under the MIT License.

---

## 💚 Built with Kotlin & Jetpack Compose
