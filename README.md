# Add2Num (A2N) — Cộng 2 số lớn dạng chuỗi

Lời giải cho challenge **A2N / Add2Num – Project Add 2 numbers** (High level requirement v1.8).

Bài toán: cộng hai số nguyên không âm rất lớn, được biểu diễn dưới dạng **chuỗi**, bằng đúng
thuật toán học sinh tiểu học đặt tính trên giấy — duyệt hai chuỗi từ phải sang trái, cộng từng
cặp chữ số, giữ số nhớ.

Điểm mấu chốt: **không** đổi chuỗi sang kiểu số. Nhờ vậy hai toán hạng có thể dài hơn nhiều so
với giới hạn của `long` hay `double`. `BigInteger` chỉ xuất hiện trong unit test, đóng vai trò
nguồn đối chiếu độc lập để kiểm tra kết quả.

```java
MyBigNumber calculator = new MyBigNumber();
calculator.sum("1234", "897");   // "2131"
```

---

## 1. Cấu trúc project

```
Add2Num/
├── pom.xml                        <- parent, gom các module lại
├── README.md
├── .gitignore
├── core/                          <- PHẦN LÕI (core): thứ được đóng gói và bàn giao
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/nguyenhuudung/add2num/MyBigNumber.java
│       └── test/java/com/nguyenhuudung/add2num/MyBigNumberTest.java
└── console/                       <- chương trình dòng lệnh để chạy thử phần core
    ├── pom.xml
    └── src/main/java/com/nguyenhuudung/add2num/console/Add2NumConsole.java
```

| Thư mục | Vai trò |
|---|---|
| `core/` | Lớp riêng `MyBigNumber` với method `String sum(String stn1, String stn2)`, đúng như đề bài. Đây là thư viện được đóng gói thành `add2num-core-0.0.1.jar` để bàn giao. Không phụ thuộc thư viện bên thứ ba nào. |
| `console/` | Đóng vai "nhóm khác làm giao diện (hoặc ứng dụng dạng console)" mà đề bài nhắc tới. Phụ thuộc vào `core` y như cách nhóm đó sẽ dùng. **Không** thuộc sản phẩm bàn giao. |

Unit test nằm ở `core/src/test`, tức một thư mục riêng, tách khỏi mã nguồn chính
`core/src/main` như đề bài khuyến khích, và không được đóng gói vào file jar bàn giao.

`MyBigNumber` không in ra màn hình và không tự cấu hình logging — đó là việc của ứng dụng gọi
nó. `Add2NumConsole` mới là nơi quyết định log hiển thị ra sao. Nhờ vậy phần lõi bàn giao cho
nhóm khác mà không áp đặt gì lên họ.

---

## 2. Yêu cầu môi trường

| Thành phần | Phiên bản | Bắt buộc? |
|---|---|---|
| JDK | 17 trở lên | Có |
| Apache Maven | 3.8 trở lên | Không — xem mục 3, cách 2 |

Không cần cài thêm gì khác. JUnit chỉ dùng ở phạm vi `test` của module `core`.

---

## 3. Cách build và chạy test

### Cách 1 — Maven (khuyên dùng)

Chạy từ **thư mục gốc** của repo (nơi có file `pom.xml` ngoài cùng):

```bash
mvn test        # biên dịch cả hai module và chạy toàn bộ unit test của core
mvn package     # tạo core/target/add2num-core-0.0.1.jar và console/target/add2num-console-0.0.1.jar
```

Lần chạy đầu Maven sẽ tải JUnit về, nên máy cần vào được Maven Central.

### Cách 2 — Chỉ dùng JDK, không cần Maven

Dùng khi máy không vào được Maven Central. Cách này biên dịch và chạy được **mã nguồn chính**,
không chạy unit test vì unit test cần JUnit. Chạy từ thư mục gốc của repo:

```bash
# Windows (CMD hoặc PowerShell)
javac -d out\core core\src\main\java\com\nguyenhuudung\add2num\MyBigNumber.java
javac -cp out\core -d out\console console\src\main\java\com\nguyenhuudung\add2num\console\Add2NumConsole.java
java -cp "out\core;out\console" com.nguyenhuudung.add2num.console.Add2NumConsole 1234 897
```

```bash
# Linux / macOS
javac -d out/core core/src/main/java/com/nguyenhuudung/add2num/MyBigNumber.java
javac -cp out/core -d out/console console/src/main/java/com/nguyenhuudung/add2num/console/Add2NumConsole.java
java -cp out/core:out/console com.nguyenhuudung.add2num.console.Add2NumConsole 1234 897
```

Từng file được liệt kê thẳng tên thay vì viết `*.java`, vì PowerShell không tự bung ký tự `*`
như bash.

---

## 4. Chạy thử

Sau `mvn package`, chạy chương trình console với hai file jar trên classpath:

```bash
# Windows
java -cp "core\target\add2num-core-0.0.1.jar;console\target\add2num-console-0.0.1.jar" com.nguyenhuudung.add2num.console.Add2NumConsole 1234 897

# Linux / macOS
java -cp core/target/add2num-core-0.0.1.jar:console/target/add2num-console-0.0.1.jar com.nguyenhuudung.add2num.console.Add2NumConsole 1234 897
```

Kết quả:

```
[LOG] sum("1234", "897") - begin
[LOG] step 1: 4 + 7 + carry 0 = 11 -> keep 1, carry 1
[LOG] step 2: 3 + 9 + carry 1 = 13 -> keep 3, carry 1
[LOG] step 3: 2 + 8 + carry 1 = 11 -> keep 1, carry 1
[LOG] step 4: 1 + 0 + carry 1 = 2 -> keep 2, carry 0
[LOG] sum("1234", "897") = "2131" in 4 step(s)
Result: 2131
```

Đối chiếu với ví dụ trong đề bài: bước 1 lấy 4 cộng 7 được 11, lưu 1 nhớ 1; bước 2 lấy 3 cộng 9
được 12, cộng tiếp nhớ 1 được 13, lưu 3 nhớ 1. Khớp.

Đổi `1234 897` thành hai số bất kỳ để thử số khác. Không truyền tham số thì chương trình chạy
sẵn ví dụ trong đề bài.

---

## 5. Đóng gói và bàn giao cho nhóm khác

Đề bài mô tả: *"Hàm này sẽ được đóng gói để bàn giao cho một nhóm khác làm giao diện (hoặc ứng
dụng dạng console) để họ gọi hàm của bạn trong dự án lớn hơn."*

Sản phẩm bàn giao là **một file duy nhất**: `core/target/add2num-core-0.0.1.jar`, sinh ra bởi
`mvn package`. Nó chỉ chứa lớp `MyBigNumber`.

**Cách 1 — qua Maven.** Từ thư mục gốc, cài các module vào kho local:

```bash
mvn install
```

Rồi dự án của nhóm kia khai báo dependency:

```xml
<dependency>
  <groupId>com.nguyenhuudung</groupId>
  <artifactId>add2num-core</artifactId>
  <version>0.0.1</version>
</dependency>
```

**Cách 2 — bỏ thẳng file jar vào classpath**, không cần Maven:

```bash
javac -cp add2num-core-0.0.1.jar -d out OtherTeamApp.java
java  -cp add2num-core-0.0.1.jar:out OtherTeamApp        # Windows dùng dấu ; thay cho :
```

Dù theo cách nào, phía họ chỉ cần viết:

```java
import com.nguyenhuudung.add2num.MyBigNumber;

public class OtherTeamApp {
    public static void main(String[] args) {
        MyBigNumber calculator = new MyBigNumber();
        System.out.println("Tổng: " + calculator.sum("1234", "897"));   // Tổng: 2131
    }
}
```

Mặc định họ sẽ thấy lịch sử phép cộng in ra theo định dạng hai dòng có kèm thời gian của
`java.util.logging`. Muốn tắt hẳn thì thêm một dòng, không phải sửa thư viện:

```java
Logger.getLogger(MyBigNumber.class.getName()).setLevel(Level.WARNING);
```

---

## 6. Thuật toán

```
i = độ dài stn1 - 1;  j = độ dài stn2 - 1;  nhớ = 0
lặp khi (i >= 0) hoặc (j >= 0) hoặc (nhớ > 0):
    trái  = (i >= 0) ? chữ số tại stn1[i] : 0
    phải  = (j >= 0) ? chữ số tại stn2[j] : 0
    tổng  = trái + phải + nhớ
    giữ lại (tổng mod 10);  nhớ = tổng div 10
    ghi log bước này;  i--;  j--
đảo ngược dãy chữ số đã giữ, bỏ số 0 vô nghĩa ở đầu, trả về
```

Ba chi tiết quyết định tính đúng đắn:

- Chuỗi nào hết chữ số thì đóng góp `0`. Đây là cách hai số **khác độ dài** khớp cột với nhau
  (1234 + 897).
- Điều kiện lặp có `nhớ > 0`. Thiếu vế này thì `999 + 1` ra `000` thay vì `1000`.
- Chữ số sinh ra theo thứ tự từ hàng đơn vị, nên phải **đảo ngược** trước khi trả về.

Độ phức tạp: thời gian `O(n)`, bộ nhớ `O(n)` với `n` là độ dài chuỗi dài hơn.

---

## 7. Giả định và các quyết định đã chốt

| # | Vấn đề | Quyết định |
|---|---|---|
| 1 | Dữ liệu đầu vào | Theo đề bài, giả định tham số luôn hợp lệ: không rỗng, chỉ chứa ký tự `0`–`9`, không dấu, không khoảng trắng. Hàm **không kiểm tra** và **không xử lý lỗi dữ liệu**. |
| 2 | Hai số khác độ dài | Chuỗi ngắn hơn được coi như có chữ số `0` ở các vị trí còn thiếu. |
| 3 | Còn nhớ sau chữ số cuối | Thêm chữ số nhớ vào đầu kết quả. `999 + 1 = 1000`. |
| 4 | Số `0` thừa ở đầu toán hạng | Kết quả được chuẩn hóa: `sum("007", "3")` trả `"10"`, không phải `"010"`. Giá trị 0 vẫn giữ một chữ số: `sum("0", "0")` trả `"0"`. |
| 5 | Có dùng `BigInteger` / `parseInt` không | Không, trong mã nguồn chính. Làm vậy là đi ngược mục tiêu của bài và tràn số với chuỗi dài. `BigInteger` chỉ dùng trong test để đối chiếu. |
| 6 | Ghi log | Dùng `java.util.logging` (đề bài ưu tiên LOGGING hơn PRINT). Mỗi bước cộng một dòng ở mức `INFO`. |
| 7 | Mức log `INFO` có ồn không | Có, nếu nhúng vào hệ thống lớn. Đổi lại, lịch sử phép toán hiện ra ngay mà không cần cấu hình gì — đúng thứ đề bài yêu cầu. Bên gọi muốn tắt thì: `Logger.getLogger(MyBigNumber.class.getName()).setLevel(Level.WARNING);` |
| 8 | An toàn đa luồng | `MyBigNumber` không giữ trạng thái nên dùng chung giữa nhiều luồng được. |

---

## 8. Bộ unit test

`MyBigNumberTest` gồm 11 test, chia hai lớp.

Mười test đặt tên rõ ràng, mỗi test khóa một hành vi:

| # | Phép tính | Kết quả mong đợi | Nhằm kiểm tra |
|---|---|---|---|
| 1 | `1234 + 897` | `2131` | Ví dụ trong đề bài |
| 2 | `897 + 1234` | `2131` | Đổi chỗ hai toán hạng khác độ dài |
| 3 | `0 + 0` | `0` | Không trả về chuỗi rỗng |
| 4 | `123 + 0`, `0 + 456` | `123`, `456` | Cộng với 0 |
| 5 | `5 + 5` | `10` | Nhớ ở chữ số cuối, kết quả dài thêm |
| 6 | `999 + 1` | `1000` | Nhớ lan truyền qua dãy số 9 |
| 7 | `99999 + 99999` | `199998` | Nhớ ở mọi vị trí |
| 8 | `12345678901234567890 + 98765432109876543210` | `111111111011111111100` | Vượt xa phạm vi `long` |
| 9 | `1 + 999…9` (30 chữ số 9) | `1` và 30 số `0` | Nhớ chạy hết chuỗi |
| 10 | `007 + 3`, `000 + 0` | `10`, `0` | Chuẩn hóa số 0 ở đầu |

Test thứ 11 sinh **2000 cặp số ngẫu nhiên** dài tới khoảng 60 chữ số và so kết quả với
`BigInteger.add`. Seed cố định (`20260919`) để mỗi lần chạy đều tái hiện được y hệt — một test
ngẫu nhiên không tái hiện được thì không dùng để truy lỗi được.

Hai lớp này bù cho nhau: test đặt tên nói rõ *ý định*, test ngẫu nhiên bắt những lỗi số nhớ mà
người viết test thường không nghĩ tới.

---

## 9. Phiên bản và cách nộp

Phiên bản hoàn thành để đánh giá là **0.0.1**, đánh dấu bằng **git tag** `0.0.1`
(đề bài cho phép dùng tag hoặc branch).

```bash
git init
git add .
git commit -m "Add MyBigNumber.sum with unit tests and README"
git branch -M main

git remote add origin https://github.com/DungK5UIT/Add2Num.git
git push -u origin main

git tag 0.0.1
git push origin 0.0.1
```

Repo phải để chế độ **công khai (public)** thì người chấm mới xem được.

### Tự kiểm tra trong vai người khác

Đề bài yêu cầu sau khi làm xong phải đóng vai một người khác, clone mã nguồn về thư mục theo
quy ước rồi chạy lại từ đầu theo README:

```bash
# Windows
git clone https://github.com/DungK5UIT/Add2Num.git "D:\Projects\github.com\DungK5UIT\Add2Num"
cd "D:\Projects\github.com\DungK5UIT\Add2Num"

# macOS / Linux
git clone https://github.com/DungK5UIT/Add2Num.git ~/Projects/github.com/DungK5UIT/Add2Num
cd ~/Projects/github.com/DungK5UIT/Add2Num
```

Rồi chạy `mvn test` (hoặc cách 2 ở mục 3) và đối chiếu với kết quả ở mục 4. Chạy được nghĩa là
README đủ để người khác làm lại được.
