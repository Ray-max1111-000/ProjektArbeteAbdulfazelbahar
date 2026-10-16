# Password Vault

**Author:** Abdulfazelbahar
**Course:** Programmering med Java · DEV26M_PJ-2

A console program that stores passwords (web logins and WiFi networks) and encrypts
images and videos. Everything is protected with AES encryption, and the program is
locked behind a 2FA code from an Authenticator app.

## How to run

```bash
javac *.java
java Main
```

Requires JDK 11 or newer.

**2FA:** add the secret key `JBSWY3DPEHPK3PXP` to Google or Microsoft Authenticator
(or set your own with the `TOTP_SECRET` environment variable), then type the 6-digit code at start.

Saved data:
- `vault.dat` – the encrypted list of all entries
- `vault_files/` – encrypted images and videos (`<name>.enc`)

Both are ignored by Git.

## Project idea

A password vault where the user can save web logins and WiFi passwords, and encrypt
images and videos with their own password. The vault is saved encrypted to disk.

## Superclass

- **Name:** `Credential` (abstract class)
- **Common fields:** `name`, `password`
- **Common methods:** `getName()`, input validation in the constructor (`requireText()`),
  and the abstract methods `getType()`, `toCsv()`, `describe()`

## Subclasses

1. **`WebLogin`** – adds a `username`. Overrides `getType()`, `toCsv()` and `describe()`
   to include the username.
2. **`WifiNetwork`** – the name is the network name (SSID). Overrides the same three methods
   with its own format.
3. **`SecureFile`** – an image or video. Adds `encrypt()` and `decrypt()`, checks that the
   file really is an image or video, and overrides the three methods to show the file path.

## Interface

- **Name:** `StrengthCheckable`
- **Method:** `boolean hasWeakPassword()`
- **Implemented by:** `WebLogin` (under 8 characters or no digit), `WifiNetwork`
  (under 8 characters, the WPA2 minimum) and `SecureFile` (under 10 characters).
- **Used polymorphically** in `VaultManager.weakPasswords()`, which loops over the list
  and calls `hasWeakPassword()` through the interface reference.

## Class structure

| Class | Responsibility |
|---|---|
| `Main` | The menu loop. Only reads input and prints output. |
| `ConsoleInput` | Reads text and numbers from the keyboard without crashing on bad input. |
| `VaultManager` | Holds the `ArrayList<Credential>` and all rules: add, search, remove, statistics, save, load. |
| `Credential` + subclasses | The data and the behaviour of each type. |
| `Crypto` | AES encryption and decryption of bytes (used for both `vault.dat` and files). |
| `Authenticator` | Checks the 2FA code (TOTP). |

A new type of credential only needs a new subclass and one new `case` in
`VaultManager.fromCsv()`. No existing code has to be rewritten.

## Menu

1. Add web login
2. Add WiFi network
3. Encrypt image/video
4. Decrypt image/video
5. View all (polymorphic: each object runs its own `describe()`)
6. Search by name
7. Remove by name
8. Statistics (entries per type and weak passwords)
9. Save & exit

## Error scenarios

- **Empty field or a comma in a field** – the `Credential` constructor throws
  `IllegalArgumentException` (a comma would break the CSV format in `vault.dat`).
- **Duplicate name** – `VaultManager.add()` throws `IllegalArgumentException`.
- **Letters or an empty answer in the menu** – `NumberFormatException` is caught in
  `ConsoleInput.number()` and the user is asked again.
- **File not found** – `NoSuchFileException` when the path is wrong.
- **Not an image or video** – `SecureFile.encrypt()` throws `IllegalArgumentException`.
- **Wrong password when decrypting** – AES throws `BadPaddingException`, shown as
  "Wrong password or damaged file".
- **Damaged `vault.dat`** – caught at start with a clear message instead of a crash.

## Motivation

This was actully an inspiration of mine from a project that i have done in the past but was not completed. I have always found encryption and how modern mathemathical algorithms and formula encrypt our daily communication from our networks to banking and simple messages its always been intresting to me and thats what has pushed me to make this simple yet effective tool i hope you find my little project intresting and perhaps useful. 
