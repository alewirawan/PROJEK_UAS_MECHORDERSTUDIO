# MerchOrderStudio 

<div align="center">
  <img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=20&duration=2600&pause=900&color=00C2A8&center=true&vCenter=true&width=500&lines=Merchandise+Ordering+System;Java+Swing+%2B+MySQL;Tugas+Akhir+UAS" alt="Typing animation" />
</div>

<div align="center">
  <img src="https://img.shields.io/badge/Java-Swing-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java badge" />
  <img src="https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL badge" />
  <img src="https://img.shields.io/badge/NetBeans-IDE-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white" alt="NetBeans badge" />
</div>

Aplikasi desktop untuk pemesanan merchandise, dibuat pakai Java Swing (NetBeans) dengan MySQL sebagai database.

## Fitur
- Registrasi & login user (data akun, no. telp, alamat)
- CRUD data produk/merchandise (kategori, harga, stok, ukuran, foto produk disimpan sebagai BLOB)
- Keranjang & pemesanan (pilih produk, jumlah, upload desain custom, catatan)
- Tracking status pesanan (pending, diproses, produksi, selesai, dikirim)
- Pembayaran per pesanan (metode bayar, upload bukti bayar, status lunas/pending)

## Tech Stack
- Java (Swing, NetBeans project)
- MySQL

## Struktur Project
```
|--- database/     # dump/skrip database MySQL
|--- src/          # source code aplikasi
|--- lib/          # library eksternal
L--- nbproject/    # konfigurasi project NetBeans
```

## Struktur Database
Database: `db_merchorderstudio`

**users**
| Kolom | Tipe | Keterangan |
|---|---|---|
| id_user | int | PK, auto increment |
| nama | varchar(255) | |
| email | varchar(255) | unique |
| password | varchar(255) | |
| no_telp | varchar(20) | |
| alamat | text | |

**produk**
| Kolom | Tipe | Keterangan |
|---|---|---|
| id_produk | int | PK, auto increment |
| nama_produk | varchar(255) | |
| kategori | varchar(100) | |
| harga | decimal(10,2) | |
| stok | int | default 0 |
| ukuran | varchar(50) | |
| deskripsi | text | |
| foto_produk | varchar(255) | path/nama file gambar |

**pesanan**
| Kolom | Tipe | Keterangan |
|---|---|---|
| id_pesanan | int | PK, auto increment |
| id_user | int | FK → users |
| tanggal_pesan | date | |
| total_harga | decimal(10,2) | |
| status_pesanan | enum | pending, diproses, produksi, selesai, dikirim |

**detail_pesanan**
| Kolom | Tipe | Keterangan |
|---|---|---|
| id_detail | int | PK, auto increment |
| id_pesanan | int | FK → pesanan |
| id_produk | int | FK → produk |
| jumlah | int | |
| upload_desain | varchar(255) | |
| catatan | text | |

**pembayaran**
| Kolom | Tipe | Keterangan |
|---|---|---|
| id_pembayaran | int | PK, auto increment |
| id_pesanan | int | FK → pesanan, unique (relasi 1-1) |
| metode_bayar | varchar(100) | |
| bukti_bayar | varchar(255) | |
| status_bayar | enum | pending, lunas |
| tanggal_bayar | date | |

Relasi:
- users -> pesanan (1 user bisa punya banyak pesanan)
- pesanan -> detail_pesanan (1 pesanan bisa punya banyak item)
- produk -> detail_pesanan (1 produk bisa muncul di banyak item pesanan)
- pesanan -> pembayaran (1 pesanan hanya punya 1 pembayaran)

## Cara Menjalankan
1. Import database dari folder `database/` ke MySQL (misal via phpMyAdmin/MySQL Workbench)
2. Sesuaikan konfigurasi koneksi database di source code (host, user, password, nama DB)
3. Buka project ini di NetBeans
4. Run project

## Note
Project ini dibuat untuk keperluan tugas kuliah.

---

<div align="center">
  <sub>Dibuat oleh</sub>
  <br><br>
  <a href="https://github.com/alewirawan"><img src="https://img.shields.io/badge/GitHub-alewirawan-181717?style=flat-square&logo=github&logoColor=white" alt="alewirawan" /></a>
  <a href="https://github.com/RajaArdika"><img src="https://img.shields.io/badge/GitHub-RajaArdika-181717?style=flat-square&logo=github&logoColor=white" alt="RajaArdika" /></a>
  <a href="https://github.com/vousvurourou"><img src="https://img.shields.io/badge/GitHub-vousvurourou-181717?style=flat-square&logo=github&logoColor=white" alt="vousvurourou" /></a>
  <br />
  <a href="https://github.com/MMahesa"><img src="https://img.shields.io/badge/GitHub-MMahesa-181717?style=flat-square&logo=github&logoColor=white" alt="MMahesa" /></a>
  <a href="https://github.com/rohimialstar"><img src="https://img.shields.io/badge/GitHub-rohimialstar-181717?style=flat-square&logo=github&logoColor=white" alt="rohimialstar" /></a>
</div>