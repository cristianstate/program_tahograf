# Program Tahograf - Jurnal de Activitate Șoferi Camion

**Program Tahograf** este o aplicație mobilă Android dezvoltată modern cu **Kotlin** și **Jetpack Compose**, creată pentru a ajuta șoferii de camion să-și înregistreze și gestioneze ușor activitatea zilnică (timpul de condus, intervalul orar de lucru și durata perioadei de odihnă).

---

##  Funcționalități Principale

1. **Înregistrare Activitate Zilnică (Dashboard Screen)**
   * Selectare dată cu `DatePickerDialog`.
   * Setare ora de început și ora de sfârșit cu `TimePickerDialog`.
   * Introducere timp de condus (ex: `4.5h`).
   * Selectare rapidă a duratei de odihnă (ex: `9h` sau `11h`).
   * Salvare directă în baza de date locală.

2. **Istoric Activități & Căutare (History Screen)**
   * Afișare listă derulabilă (`LazyColumn`) cu toate jurnalele salvate, ordonate descrescător după dată.
   * Căutare și filtrare în timp real după dată (format `YYYY-MM-DD`).
   * Afișare stare goală (*Empty State*) în cazul în care nu există înregistrări.

3. **Editare și Ștergere Jurnal (Edit Log Screen)**
   * Selectare jurnal din istoric pentru vizualizarea detaliilor și modificare.
   * Actualizarea oricărui câmp din formular.
   * Ștergerea definitivă a înregistrării din baza de date.

---

## Tehnologii și Arhitectură

* **Limbaj:** Kotlin
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) cu componente **Material 3**
* **Arhitectură:** **MVVM (Model-View-ViewModel)**
* **Persistența Datelor:** [Room Database](https://developer.android.com/training/data-storage/room)
* **Asincronism & Reactive Streams:** Kotlin Coroutines & `StateFlow` / `Flow`
* **Navigație:** Navigation3 API (`NavDisplay`, `NavEntry`)
* **Dependențe principale:**
  * `androidx.compose.material3:material3`
  * `androidx.room:room-runtime` & `androidx.room:room-ktx`
  * `androidx.lifecycle:lifecycle-viewmodel-compose`
  * `kotlinx-serialization-json`

---

##  Structura Proiectului

```
com.fitness.myapplication/
├── data/                       # Stratul de date (Room)
│   ├── TruckerLog.kt           # Entitatea Room pentru tabelul "trucker_logs"
│   ├── TruckerLogDao.kt        # Interfața DAO (interogări SQL, insert, update, delete)
│   └── TruckerLogDatabase.kt   # Clasa Singleton a bazei de date Room
│
├── navigation/                 # Configurare rute de navigare
│   └── Destinations.kt         # Destinațiile aplicației (Dashboard, History, EditLog)
│
├── ui/                         # Interfața cu utilizatorul
│   ├── screens/                # Ecranele aplicației
│   │   ├── DashboardScreen.kt  # Ecranul principal de introducere date
│   │   ├── HistoryScreen.kt    # Ecranul de istoric și căutare
│   │   └── EditLogScreen.kt   # Ecranul de editare/ștergere
│   ├── theme/                  # Culori, tipografie și temă (Material 3)
│   └── viewmodel/              # Logica de business
│       ├── TruckerLogViewModel.kt        # ViewModel-ul principal
│       └── TruckerLogViewModelFactory.kt # Factory pentru furnizarea DAO-ului
│
└── MainActivity.kt             # Activitatea principală & punctul de intrare Compose
```

---

##  Cerințe de Sistem și Rulare

* **Android Studio:** Ladybug / Jellyfish (sau o versiune recentă cu suport Jetpack Compose & Navigation3)
* **JDK:** 17 sau superior
* **Min SDK:** 26 (Android 8.0 Oreo)
* **Target SDK:** 35 (Android 15)

### Cum se rulează:
1. Clonează sau deschide proiectul în **Android Studio**.
2. Efectuează o sincronizare Gradle (**Gradle Sync**).
3. Conectează un dispozitiv fizic Android sau un emulator (API >= 26).
4. Apasă pe **Run ('app')** (`Shift + F10`).
