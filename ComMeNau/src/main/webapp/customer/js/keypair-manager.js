function createKeyPair() {
    $.ajax({
        url: '/keypair-manager?action=create-keypair',
        method: 'get',
        contentType: "charset=utf-8",
        data: {},
        success: function (response) {
            // Kiểm tra nếu response.status là "success"
            if (response.status === "success") {
                savePrivateKeyBinary(response.privateKey, response.expired, response.filename)
            } else {
                Swal.fire({
                    icon: "warning",
                    title: response.message || "Có lỗi xảy ra khi tạo khóa.",
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    timer: 1000,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                        toast.onmouseenter = Swal.stopTimer;
                        toast.onmouseleave = Swal.resumeTimer;
                    }
                });
            }
        },
        error: function (xhr, status, error) {
            // Nếu có lỗi khi gửi yêu cầu
            console.log("Error: " + error);
            Swal.fire({
                icon: "warning",
                title: "Xóa thất bại! Không thể gửi yêu cầu.",
                toast: true,
                position: "top-end",
                showConfirmButton: false,
                timer: 1000,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.onmouseenter = Swal.stopTimer;
                    toast.onmouseleave = Swal.resumeTimer;
                }
            });
        }
    })
}

async function savePrivateKeyBinary(privateKeyBase64, expired, filename) {
    try {
        // Định nghĩa header (256 byte cố định)
        const header = `${filename}|${expired}`.padEnd(256, " "); // Header gồm tên file và thời gian hết hạn, bổ sung khoảng trắng nếu thiếu.

        // Mã hóa header sang nhị phân
        const encoder = new TextEncoder();
        const headerBytes = encoder.encode(header);

        // Mã hóa body (private key) sang nhị phân
        const privateKeyBytes = Uint8Array.from(atob(privateKeyBase64), (c) => c.charCodeAt(0));

        // Kết hợp header và body
        const fileContent = new Uint8Array(headerBytes.length + privateKeyBytes.length);
        fileContent.set(headerBytes, 0); // Chèn header
        fileContent.set(privateKeyBytes, headerBytes.length); // Chèn body

        // Hiển thị hộp thoại cho người dùng chọn nơi lưu file
        const fileHandle = await window.showSaveFilePicker({
            suggestedName: `${filename}.key`,
            types: [
                {
                    description: "Binary Key File",
                    accept: { "application/octet-stream": [".key"] },
                },
            ],
        });

        // Mở file và ghi dữ liệu
        const writable = await fileHandle.createWritable();
        await writable.write(fileContent);
        await writable.close();

        console.log("Private key đã được lưu thành công.");
        Swal.fire({
            icon: "success",
            title: "Khóa đã được tạo thành công",
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 1000,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.onmouseenter = Swal.stopTimer;
                toast.onmouseleave = Swal.resumeTimer;
            }
        });
    } catch (error) {
        console.error("Lỗi khi lưu private key:", error);
        Swal.fire({
            icon: "error",
            title: "Lỗi khi lưu private key.",
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
        });
    }
}

async function getPrivateKeyBinary() {
    try {
        // Hiển thị hộp thoại để người dùng chọn file
        const [fileHandle] = await window.showOpenFilePicker({
            types: [
                {
                    description: "Binary Key File",
                    accept: { "application/octet-stream": [".key"] },
                },
            ],
            multiple: false,
        });

        // Đọc nội dung file
        const file = await fileHandle.getFile();
        const arrayBuffer = await file.arrayBuffer();
        const data = new Uint8Array(arrayBuffer);

        // Định nghĩa độ dài cố định cho header
        const HEADER_LENGTH = 256; // Số byte cố định cho header

        // Kiểm tra độ dài file
        if (data.length <= HEADER_LENGTH) {
            throw new Error("File không hợp lệ hoặc không có nội dung.");
        }

        // Đọc header
        const headerBytes = data.slice(0, HEADER_LENGTH);

        // Giải mã header (tên file và thời gian hết hạn)
        const decoder = new TextDecoder();
        const headerString = decoder.decode(headerBytes).trim();
        const [fileName, expired] = headerString.split("|");

        if (!fileName || !expired) {
            throw new Error("Header không hợp lệ.");
        }

        // Kiểm tra xem file có hết hạn chưa
        const expiredDate = new Date(expired);
        if (isNaN(expiredDate.getTime()) || new Date() > expiredDate) {
            throw new Error("File đã hết hạn.");
        }

        // Đọc body (phần chứa private key)
        const privateKeyBytes = data.slice(HEADER_LENGTH);
        const privateKeyBase64 = btoa(String.fromCharCode(...privateKeyBytes));

        console.log("Private key đã được đọc thành công:", privateKeyBase64);
        console.log("Tên file:", fileName);
        console.log("Thời gian hết hạn:", expired);

        // Trả về private key dưới dạng Base64
        return {
            fileName,
            expired,
            privateKey: privateKeyBase64,
        };
    } catch (error) {
        console.error("Lỗi khi đọc private key:", error);
        Swal.fire({
            icon: "error",
            title: error.message || "Lỗi khi đọc private key.",
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: true,
        });
        throw error; // Rethrow để xử lý nếu cần
    }
}