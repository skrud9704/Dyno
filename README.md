# Dyno (Diet + Note)
Konkuk Univ. CSE Graduation Project

Mobile service that helps Medication / Health Supplements intake management

--

### Intro

* Interest in medicines and health supplementary foods is increasing day by day.
* According to the Korea Health Supplements Association (KHSA), the domestic health Supplement market formed 4.6 trillion won in 2019.
* However, the number of reports of side effects from their misuse is increasing every year.
* In particular, health supplements can be purchased without a doctor's prescription, so it is difficult for the general public to access information on the prohibition of medical use between drugs or health supplements.
* So we developed a service that helps safe intake of medicines and health supplements.


--

### Funcions

##### 1. Medicines / Health Supplements intake management
* You can manage both medicine and supplements in one application.
* Your dosage information is only stored on the device. Never sent to server.
![그림1](https://user-images.githubusercontent.com/43378081/110243808-5656f880-7f9f-11eb-93c4-a84cd3530b46.png)


##### 2. Use OCR to register / edit conveniently
* You can register medicine by taking a picture of the prescription or medicine bag.
* You can register supplements by taking a picture of the product.
* We also provide a name search function for unrecognized drugs.
* When OCR recognition has done, we recognizes medicine and disease information from the picture and stores it on the device.

##### 3. Probability model-based disease estimation
* About medicine, in case of drug bag or some prescription, there's no information about disease. 
* So we developed disease estimation algorithm that uses "Prescription records by pharmacology group" provided by Health Insurance Review & Assessment Service

##### 4. DUR (Drug Utilization Review) between medicine and supplements
* Many existing services have provided DUR between medications, But we also provide DUR functions between supplements and medicines.

##### 5. Notify warning in advance
* We inform you of the main ingredients of supplements that should not be taken together when registering medicine.


-- 

### Development

[Client]
* FrameWork : Android
* Language : Kotlin
* API : Retrofit2, Okhttp3

[Server]

--

### How to Use App

* Not servicing now.

--

### Contack

* Mail : skrud9704@gmail.com
