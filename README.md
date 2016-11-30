# Google Protocol Buffer

- [Khái quát](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#i-khái-quát)
- [So sánh với các kiểu truyền tải dữ liệu khác](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#ii-so-sánh-với-các-kiểu-truyền-tải-dữ-liệu-khác)
- [Hoạt động](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#iii-hoạt-động)
- [Tutorial](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#iv-tutorials)
- [Mở rộng PB](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#v-mở-rộng-pb)
- [Advanced: Java Generated Code](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#vi-advanced-java-generated-code)
	+ [Compiler](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#compiler)
	+ [Packages](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#packages)
	+ [Messages](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#messages)
	+ [Fields](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#fields)
	+ [Any](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#any)
	+ [Enumerations](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#enumerations)
	+ [Exensions (chỉ ở proto2)](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#exensions-chỉ-ở-proto2)
	+ [Services](https://github.com/quytm/protocol-buffer-google/blob/master/README.md#services)
	

## I. Khái quát

- Khái niệm: 
	+ Protocol Buffer (PB) là một phương pháp tuần tự hóa(serializing) dữ liệu có cấu trúc. Nó hữu dụng cho việc truyền tải dữ liệu hoặc lưu trữ dữ liệu.
	+ Serializing (tuần tự hóa): Dữ liệu được chuyển, mã hóa sang dạng chuỗi bit, sau đó được tạo lại ở bên nhận.
	+ Google PB hỗ trợ việc generate code cho nhiều ngôn ngữ để sử dụng Protocol Buffer.

- Other:
	+ BPB linh hoạt, hiệu quả và tự động cơ cấu cho dữ liệu tuần tự -> so với XML nó nhỏ hơn, nhanh hơn và đơn giản hơn.

## II. So sánh với các kiểu truyền tải dữ liệu khác
- PB dịch dữ liệu ra chuỗi bit.
- So với XML:
	+ Nhỏ hơn 3-10 lần.
	+ Nhanh hơn 20-100 lần.
	+ Dễ dàng truy cập hơn.
- So với JSON: PB tốn CPU của server hơn, nhưng ngốn CPU ít hơn rất nhiều ở client, tốc độ truyền tải cũng nhanh hơn đáng kể.
- Nhưng JSON lại diễn giải dữ liệu dễ hiểu hơn, PB dịch ra chuỗi bit nên k đọc đc

## III. Hoạt động

- Định dạng dữ liệu thông qua file `.proto`, nó lưu thông tin chứa một loạt các `name-value`
- Ví dụ:
```
message Person {
  required string name = 1;
  required int32 id = 2;
  optional string email = 3;

  enum PhoneType {
    MOBILE = 0;
    HOME = 1;
    WORK = 2;
  }

  message PhoneNumber {
    required string number = 1;
    optional PhoneType type = 2 [default = HOME];
  }

  repeated PhoneNumber phone = 4;
}
```

- Có thể add thêm trường mới mà không bị phá vỡ cấu trúc cũ.

## IV. Tutorials

Các bước tạo một app simple:
- Định dạng format cho message trong file .proto
- Sử dụng PB compiler.

```
	protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/addressbook.proto
```

	- --java_out: options dành cho chương trình sử dụng ngôn ngữ java
	- dst_dir: nơi chứa source, link tới package_name ở trong addressbook.proto

- Sử dụng PB API để viết và đọc message: file *.java được sinh ra và đã định nghĩa hết các thuộc tính, hàm, builder, ... .
- Add file thư viện *.jar của PB vào project.
- Viết chương trình. 

**Chú ý**: Có thể sử dụng pluggin cho protoc (chưa tìm hiểu).

## V. Mở rộng PB

Trong dự án, sẽ có lúc ta cần phải mở rộng PB để đáp ứng thêm các chức năng khác, và PB có thể mở rộng ra nhưng theo 4 luật sau đây:

- **Không được** thay đổi **tag number** của các trường hiện có.
- **Không được** thêm hoặc xóa các trường **require**
- **Có thể** xóa các trường **optional** hoặc **repeated**
- **Có thể** thêm các trường **optional** hoặc **repeated** nhưng phải sử dụng **tag number** mới (không trùng với các trường đã có).

## VI. Advanced: Java Generated Code

### Compiler

- Command line:

```
	protoc --proto_path=src --java_out=build/gen src/foo.proto
```

- Tên file output định nghĩa API được viết trong `java_outer_classname`

```
	option java_outer_classname = "Foo";
```

### Packages

- Tên package định nghĩa trong `java_package`
- E.g. 

```
	package foo.bar;
	option java_package = "com.example.foo.bar";
```

, khi đó file output được đưa vào package `com.example.foo.bar`

### Messages
- Message đơn giản:
```
	message Foo {}
```
- Có một số tùy chọn khác:
	+ `option optimize_for = CODE_SIZE`: message chỉ override các method quan trọng, cần thiết của `GeneratedMessage` thay vì override toàn bộ từ đó.
	+ `option optimize_for = LITE_RUNTIME`: message chỉ impliment `MessageLite` thay vì `Message`.

- Builders: ...
- SubBuilders: ...
- Nested Types: Message lồng nhau
```
	message Foo { message Bar { } }
```

### Fields

#### Singular Fields
- Là các thuộc tính có trong Message
- Ví dụ:
```
	optional int32 foo = 1;	// proto2	
	required int32 foo = 1; 	// proto2
	int32 foo = 1;			// proto3
```

- Sau khi khai báo các trường (fields) thì API sẽ cung cấp cho các phương thức để ta thao tác trên đó: hasFoo, getFoo, clearFoo, getFooBuilder, getFooOrBuilder, ...

#### Repeated Fields
- Là các mảng dữ liệu trong Message
- Ví dụ:
```
	repeated int32 foo = 1;
```
- API cung cấp: getFooCount, getFoo, getFooList, setFoo, addFoo, addAllFoo, getFooOrBuilder, getFooBuilder, ...

#### Oneof Fields
- Ví dụ: 
```
	oneof oneof_name {
    		int32 foo = 1;
    		...
	}
```

- Chưa hiểu lắm về mục đích tạo ra thằng này.

#### Map Fields
- Giống như Map, HashMap trong java.
- Ví dụ:
```
	map<int32, int32> weight = 1;
```

- API cung cấp: getWeightMap, getWeightOrDefault, containsWeight, getWeightCount, putWeight, putAllWeight, removeWeight, clearWeight, ... .

### Any
...

### Enumerations
- Định nghĩa các `enum`.
- Ví dụ:
```
	enum Foo {
 		VALUE_A = 0;
  		VALUE_B = 5;
  		VALUE_C = 1234;
	}
```

### Exensions (chỉ ở proto2)
(bỏ qua)

### Services
- Để sử dụng service ta cần thêm tùy chọn:
```
	option java_generic_services = true;
```

- Sử dụng RPC (???)

#### Interface
- Ví dụ: 
```
	service Foo {
  		rpc Bar(FooRequest) returns(FooResponse);
	}
```

- Một Abstract class Foo được tạo ra đại diện cho service:
```
	abstract void bar(RpcController controller, FooRequest request, RpcCallback<FooResponse> done);
```

- Cần tìm hiểu thêm về RPC -_-

#### Stub
...

#### Blocking Interfaces

- outer_class_scope: Member declarations that belong in the file's outer class.
- class_scope:TYPENAME: Member declarations that belong in a message class. TYPENAME is the full proto name, e.g. package.MessageType.
- builder_scope:TYPENAME: Member declarations that belong in a message's builder class. TYPENAME is the full proto name, e.g. package.MessageType.
- enum_scope:TYPENAME: Member declarations that belong in an enum class. TYPENAME is the full proto enum name, e.g. package.EnumType.


