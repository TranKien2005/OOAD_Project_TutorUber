const API_BASE = "http://localhost:8080/api";

function pageByRole(role) {
  const r = (role || "").toUpperCase();
  const map = { ADMIN: "adminHome.html", TUTOR: "HomeTutor.html", STUDENT: "HomeStudent.html" };
  return map[r] || map.STUDENT;
}

async function login() {
  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value;
  const btn = document.getElementById("login-button");
  const msg = document.getElementById("loginMessage");

  if (!username || !password) {
    (msg ? (msg.textContent = "Vui lòng nhập đầy đủ thông tin.") : alert("Vui lòng nhập đầy đủ thông tin."));
    return;
  }

  btn.disabled = true;
  if (msg) msg.textContent = "Đang đăng nhập...";

  try {
    const res = await fetch(`${API_BASE}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    });

    let json = {};
    try { json = await res.json(); } catch { }

    if (!res.ok || !json?.success) {
      const text = json?.message || "Đăng nhập thất bại!";
      (msg ? (msg.textContent = text) : alert(text));
      return;
    }

    const token = json?.data?.token || "";
    const user = json?.data?.user || {};
    const role = user?.role || "STUDENT";

    if (token) localStorage.setItem("token", token);
    localStorage.setItem("loggedIn", "true");
    localStorage.setItem("role", role.toUpperCase());
    localStorage.setItem("user", JSON.stringify(user));

    window.location.href = pageByRole(role);
  } catch (e) {
    console.error(e);
    const text = "Lỗi kết nối.";
    (msg ? (msg.textContent = text) : alert(text));
  } finally {
    btn.disabled = false;
  }
}
