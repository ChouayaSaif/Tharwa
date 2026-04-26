# 🌱 THARWA – AI & Blockchain-Based Food Traceability System
"Dig deeper. Know more. Earn more."

<img width="532" height="252" alt="image" src="https://github.com/user-attachments/assets/43e579cf-3112-4ce0-8563-4cbf6441bd03" />


## Overview

THARWA is a mobile application that combines **Artificial Intelligence**, **Blockchain**, and **Cybersecurity** to improve **food transparency, safety, and trust**.

The system enables:
- Farmers to register food batches securely
- Consumers to scan and analyze products
- Direct marketplace interaction without intermediaries

## Project Goal

To create a **secure, transparent, and intelligent food ecosystem** where:
- Consumers trust what they eat
- Farmers gain direct market access
- Data remains private and verifiable


## Core Features

### 👨‍🌾 Farmer Module
- Register food batches on blockchain
- Add products to marketplace
- Generate QR codes linked to batch data

<p align="center">
  <img src="https://github.com/user-attachments/assets/223f3c80-0642-4b52-8db1-ba52b207993d" width="200"/>
  <img src="https://github.com/user-attachments/assets/bd4d35e4-ffde-4494-a1fc-6e7958e8f581" width="200"/>
  <img src="https://github.com/user-attachments/assets/fb780342-648e-4da1-8c8c-a8eb4e51b74b" width="200"/>
  <img src="https://github.com/user-attachments/assets/753ffc52-9e49-4549-8495-657f454a580f" width="200"/>
</p>

### 👤 Consumer Module
- Scan food (QR / image)
- Analyze food against user health profile
- Get instant results:
  - ✅ Safe  
  - ⚠️ Caution  
  - ❌ Unsafe  

<p align="center">
  <img src="https://github.com/user-attachments/assets/ed067df2-a4cd-4050-a9e1-cccbd90b086e" width="180"/>
  <img src="https://github.com/user-attachments/assets/faae4d97-516e-4f82-b54c-cb9b90bf32b6" width="180"/>
  <img src="https://github.com/user-attachments/assets/e70cd31c-c851-4229-8987-522678caa968" width="180"/>
  <img src="https://github.com/user-attachments/assets/4ec21dbb-780b-41f5-be77-350af0c65c7a" width="180"/>
  <img src="https://github.com/user-attachments/assets/801c78f0-aecb-47eb-8091-38dcd7029f92" width="180"/>
</p>

### 🛒 Marketplace
- Farmers publish products by category
- Users browse and purchase items
- Transactions simulated using blockchain principles

<p align="center">
  <img src="https://github.com/user-attachments/assets/d5415ca1-0510-42d2-b986-f3332093df7a" width="220"/>
</p>

## 🔗 Blockchain Integration

Smart contracts implemented using **Solidity** (tested in Remix IDE).

The figure shows the smart contract system where farmers register batches, certifiers validate them, and consumers access verified data, ensuring traceability and trust on the blockchain.

<p align="center">
  <img width="843" height="326" alt="image" src="https://github.com/user-attachments/assets/dbde9a31-0553-47dc-8702-ef8b485db2ac" />
</p>

### Example: Batch Registry
- Stores:
  - Crop type
  - Origin
  - Certification
  - Farmer address

### Purpose:
- Ensure **immutability**
- Enable **traceability**
- Increase **trust**


## 🤖 Local AI Model (Privacy-First)

- AI runs **entirely on-device** (**TensorFlow Lite**)
- No external API calls
- No user data leaves the device

### Benefits:
- 🔒 Privacy protection  
- ⚡ Faster processing  
- 🌐 Offline capability  

---

## 🔐 Security Implementation

### 🧼 Input Sanitization
- Removes unsafe content
- Limits input size and characters

---

### 🛡️ Prompt Guard
- Filters malicious or unsafe instructions
- Prevents prompt injection attempts

---

### 🔒 AES Encryption
- Sensitive data encrypted locally
- Ensures data confidentiality

---

### 🔑 JWT Authentication (Simulated)
- Secure communication structure for APIs
- Token-based authentication model

---

### 🔗 QR Code Signing (HMAC)
- QR codes contain signed batch data
- Prevents tampering and fraud

---

## 🏗️ System Architecture
---

## 🛠️ Tech Stack

- **Android (Kotlin)**
- **TensorFlow Lite (Local AI)**
- **Solidity (Smart Contracts)**
- **Remix IDE (Testing)**
- **AES / HMAC (Security)**
- **Retrofit (Optional API Simulation)**

---

## 📸 Demo

> Add screenshots here:
- Smart contract deployment
- QR scanning interface
- AI analysis result

---

## 🎯 Future Improvements

- Real blockchain deployment (Ethereum / Hyperledger)
- Advanced AI model training
- Full payment system integration
- Scalable backend architecture

---
