const API_BASE = "http://localhost:8080/api";

async function signup() {
  const fullName = document.getElementById("name").value.trim();
  const email = document.getElementById("email").value.trim();
  const phone = document.getElementById("phone").value.trim();
  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value;
  const role = document.getElementById("role").value;
  const btn = document.getElementById("register-button");
  const msg = document.getElementById("registerMessage");

  if (!fullName || !username || !password || !role) {
    alert("Vui lòng nhập Họ tên, Tên đăng nhập, Mật khẩu và chọn Vai trò!");
    return;
  }
  if (email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!re.test(email)) { alert("Email không hợp lệ!"); return; }
  }

  btn.disabled = true;
  msg.textContent = "Đang tạo tài khoản...";

  try {
    const res = await fetch(`${API_BASE}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password, fullName, email, phone, role })
    });

    // Nếu server trả về non-JSON, tránh throw
    let json = {};
    try { json = await res.json(); } catch { /* bỏ qua */ }

    if (!res.ok || !json?.success) {
      const serverMsg = json?.message || `${res.status} ${res.statusText}`;
      msg.textContent = `Đăng ký thất bại: ${serverMsg}`;
      return;
    }

    msg.textContent = "Đăng ký thành công! Đang chuyển sang đăng nhập...";
    // register.html nằm trong thư mục html -> chuyển cùng thư mục
    setTimeout(() => { window.location.href = "login.html"; }, 600);
  } catch (e) {
    console.error("Fetch error:", e);
    msg.textContent = "Lỗi kết nối.";
  } finally {
    btn.disabled = false;
  }
}