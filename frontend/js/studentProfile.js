const API_BASE = "http://localhost:8080/api";
const AUTH_BASE = `${API_BASE}/auth`;
const state = { original: null, editable: false };

function $(id) { return document.getElementById(id); }

function setEditable(enabled) {
  state.editable = enabled;
  ["avatar", "preferenceSubjects", "budgetMin", "budgetMax", "location", "bio"].forEach(id => {
    const el = $(id);
    if (!el) return;
    el.disabled = !enabled;
  });
  if ($("saveBtn")) $("saveBtn").disabled = !enabled;
}

function setAvatar(url) {
  const img = $("avatarImg");
  if (img) img.src = url && url.trim() ? url.trim() : "../images/default-avatar.png";
}

function fillUserHeader(user) {
  if ($("helloText")) $("helloText").textContent = `Xin chào, ${user.fullName || user.username || "Học viên"}!`;
  if ($("usernameText")) $("usernameText").textContent = `Tên đăng nhập: ${user.username || ""}`;
  if ($("emailText")) $("emailText").textContent = `Email: ${user.email || ""}`;
  if ($("phoneText")) $("phoneText").textContent = `SĐT: ${user.phone || ""}`;
}

function fillForm(profile) {
  // CHÚ Ý: dùng 'avatar' thay vì 'avatarUrl'; 'preferenceSubjects' là chuỗi CSV
  if ($("avatar")) $("avatar").value = profile.avatar || profile.avatarUrl || "";
  if ($("preferenceSubjects")) $("preferenceSubjects").value =
    Array.isArray(profile.preferenceSubjects) ? profile.preferenceSubjects.join(", ") : (profile.preferenceSubjects || "");
  if ($("budgetMin")) $("budgetMin").value = profile.budgetMin ?? "";
  if ($("budgetMax")) $("budgetMax").value = profile.budgetMax ?? "";
  if ($("location")) $("location").value = profile.location || "";
  if ($("bio")) $("bio").value = profile.bio || "";
  setAvatar(profile.avatar || profile.avatarUrl || "");
}

function readForm() {
  return {
    avatar: ($("avatar")?.value || "").trim(),                 // string
    preferenceSubjects: ($("preferenceSubjects")?.value || "") // string CSV
      .split(",").map(s => s.trim()).filter(Boolean).join(","),
    budgetMin: Number($("budgetMin")?.value || 0) || 0,
    budgetMax: Number($("budgetMax")?.value || 0) || 0,
    location: ($("location")?.value || "").trim(),
    bio: ($("bio")?.value || "").trim()
  };
}

// GET /api/students/{id}/profile
async function loadProfile() {
  const token = localStorage.getItem("token");
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const role = (localStorage.getItem("role") || "").toUpperCase();
  if (!token || role !== "STUDENT") {
    location.href = "login.html";
    return;
  }
  fillUserHeader(user);

  try {
    const res = await fetch(`${API_BASE}/students/${user.id}/profile`, {
      headers: { "Authorization": `Bearer ${token}` }
    });

    if (res.status === 401) {
      localStorage.clear();
      location.href = "login.html";
      return;
    }

    const text = await res.text();
    let json = {};
    try { json = JSON.parse(text); } catch { }

    const profile = (res.ok && json?.data) ? json.data : {
      avatar: user.avatar || user.avatarUrl || "",
      preferenceSubjects: "",
      budgetMin: 0,
      budgetMax: 0,
      location: "",
      bio: ""
    };

    state.original = profile;
    fillForm(profile);
    setEditable(false);
  } catch (e) {
    console.error("Load profile error:", e);
    state.original = {
      avatar: user.avatar || user.avatarUrl || "",
      preferenceSubjects: "",
      budgetMin: 0,
      budgetMax: 0,
      location: "",
      bio: ""
    };
    fillForm(state.original);
    setEditable(false);
  }

  const avatarInput = $("avatar");
  if (avatarInput) {
    avatarInput.addEventListener("input", (e) => setAvatar(e.target.value));
  }
}

// PUT /api/students/{id}/profile
async function saveProfile() {
  const token = localStorage.getItem("token");
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const body = readForm();

  if (body.budgetMin && body.budgetMax && body.budgetMin > body.budgetMax) {
    alert("Ngân sách tối thiểu không được lớn hơn tối đa.");
    return;
  }

  try {
    const res = await fetch(`${API_BASE}/students/${user.id}/profile`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      // Khớp UpdateProfileRequest: avatar (string), preferenceSubjects (string CSV)
      body: JSON.stringify(body)
    });

    const text = await res.text();
    let json = {};
    try { json = JSON.parse(text); } catch { }

    if (!res.ok) {
      console.warn("Update profile error:", res.status, text);
      alert(json?.message || "Cập nhật hồ sơ thất bại!");
      return;
    }

    state.original = json.data || body;
    fillForm(state.original);
    setEditable(false);
    alert("Đã cập nhật hồ sơ thành công!");
  } catch (e) {
    console.error("Save profile error:", e);
    alert("Lỗi kết nối khi lưu hồ sơ!");
  } finally {
    closeConfirm();
  }
}

function openConfirm() { if ($("confirmModal")) $("confirmModal").style.display = "flex"; }
function closeConfirm() { if ($("confirmModal")) $("confirmModal").style.display = "none"; }

function openChangePwd() {
  if ($("changePwdMessage")) $("changePwdMessage").textContent = "";
  if ($("oldPassword")) $("oldPassword").value = "";
  if ($("newPassword")) $("newPassword").value = "";
  if ($("confirmPassword")) $("confirmPassword").value = "";
  if ($("changePwdPanel")) $("changePwdPanel").style.display = "flex";
}
function closeChangePwd() {
  if ($("changePwdPanel")) $("changePwdPanel").style.display = "none";
}

// PUT /api/auth/change-password/{userId} với current/new/confirm
async function changePassword() {
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const userId = user?.id;
  const token = localStorage.getItem("token");

  const currentPassword = ($("oldPassword")?.value || "").trim();
  const newPassword = ($("newPassword")?.value || "").trim();
  const confirmPassword = ($("confirmPassword")?.value || "").trim();

  if (!currentPassword || !newPassword || !confirmPassword) {
    if ($("changePwdMessage")) $("changePwdMessage").textContent = "Vui lòng nhập đầy đủ mật khẩu cũ, mới và xác nhận.";
    return;
  }
  if (newPassword.length < 6) {
    if ($("changePwdMessage")) $("changePwdMessage").textContent = "Mật khẩu mới phải từ 6 ký tự trở lên.";
    return;
  }
  if (newPassword !== confirmPassword) {
    if ($("changePwdMessage")) $("changePwdMessage").textContent = "Xác nhận mật khẩu không khớp.";
    return;
  }

  const ok = confirm("Xác nhận đổi mật khẩu?");
  if (!ok) return;

  try {
    const res = await fetch(`${AUTH_BASE}/change-password/${userId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        ...(token ? { "Authorization": `Bearer ${token}` } : {})
      },
      body: JSON.stringify({ currentPassword, newPassword, confirmPassword })
    });
    const text = await res.text();
    let json = {};
    try { json = JSON.parse(text); } catch { }

    if (!res.ok) {
      const msg = json?.message || (res.status === 400 ? "Dữ liệu không hợp lệ hoặc mật khẩu cũ sai." : `Đổi mật khẩu thất bại! (${res.status})`);
      if ($("changePwdMessage")) $("changePwdMessage").textContent = msg;
      return;
    }

    alert("Đổi mật khẩu thành công!");
    closeChangePwd();
  } catch (e) {
    console.error("Change password error:", e);
    if ($("changePwdMessage")) $("changePwdMessage").textContent = "Lỗi kết nối. Vui lòng thử lại.";
  }
}

document.addEventListener("DOMContentLoaded", () => {
  loadProfile();
  $("editBtn")?.addEventListener("click", () => { fillForm(state.original || {}); setEditable(true); });
  $("saveBtn")?.addEventListener("click", () => openConfirm());
  $("cancelSave")?.addEventListener("click", () => { closeConfirm(); fillForm(state.original || {}); setEditable(false); });
  $("confirmSave")?.addEventListener("click", () => saveProfile());
  $("changePwdBtn")?.addEventListener("click", openChangePwd);
  $("cancelChangePwd")?.addEventListener("click", closeChangePwd);
  $("confirmChangePwd")?.addEventListener("click", changePassword);
});