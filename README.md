Addressing PCI DSS v3 Req. 8.2.1 in Java
========================================

Inspired by: https://t-rob.net/2013/02/17/its-time-for-sensible-password-security-standards-in-the-pci-dss/

The utility mainly address the requirement 8.2.1 in PCI DSS v3.0 - https://www.pcisecuritystandards.org/documents/PCI_DSS_v3.pdf

> Render all passwords unreadable during transmission and storage on all system components using strong cryptography

More specifically, the testing procedure 8.2.1b states:

> 8.2.1.b For a sample of system components, examine password files to verify that passwords are unreadable during storage

No matter how, _outbound password_ will need to be in plain text format at some point before you connect to an external system. Therefore, the encrypted storage of the password to the external system will need to be reversible.

So what can we do?
* Encrypt the password with symmetric encryption and store that in the _password file_.
* When the password is required, retrieve and decrypt the encrypted password with the symmetric encryption in the code.

Using AES encryptiong as an example, this still leave use with `key` and `iv`, which in a lot of ways are just like passwords. We put this in the code.

This means hard-coding AES `key` and `iv` in the code.

Not very effective eh? I think so too. But this seems to make the PCI people happy.

How To Use
----------
**Step 1.** Generate `key` and `iv` using `openssl`.

```sh
openssl enc -aes-256-cbc -k <some random passphase> -P -md sha
```

**Step 2.** Replace the `key` and `iv` value in the `FixPciDssv3Req821` class with the ones generated with the command above. 

```java
 private static final byte[] key = decodeHex("DC84F118EF16A7B1D586FA15D4D3F659EFD19C3FBC010CE14A2D7B20E24BEDD0");
 private static final byte[] iv = decodeHex("0AC2F416A3FEEA8CEFB8492200B953C9");
```

Don't worry about the `salt` value. It is just use in combination with the value `<some randome passphase>` you used in the `-k` argement to generate the `key` value.

**Step 3.** Choose a password and encrypt it using openssl.

```sh
echo -n "<your password>" | openssl enc -aes-256-cbc -e -K <key in hex tring format> -iv <salt in hex string format> -a
```

The output will be in `base64` format. Storge this encrypted password in the _password files_.

**Step 4.** Retrieve and decrypt the encrypted-password in your code using `FixPciDssv3Req821.decrypt(String)`

Sample Code
-----------

See [`src/main/java/org/github/lsiu/pcidss/SampleApp.java`](src/main/java/org/github/pcidss/SampleApp.java) for a code exmaple