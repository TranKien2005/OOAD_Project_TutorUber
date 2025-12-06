// ...existing code...
const API_BASE = "http://localhost:8080/api";

const state = { original: null, editable: false };

function $(id) { return document.getElementById(id); }
function setEditable(enabled) {
  state.editable = enabled;
  ["avatarUrl","preferenceSubjects","budgetMin","budgetMax","location","bio"].forEach(id => {
    const el = $(id);
    if (!el) return;
    el.disabled = !enabled;
  });
  $("saveBtn").disabled = !enabled;
}
function setAvatar(url) {
  $("avatarImg").src = url && url.trim() ? url.trim() : "../images/default-avatar.png";
}
function fillUserHeader(user) {
  $("helloText").textContent = `Xin chào, ${user.fullName || user.username || "Học viên"}!`;
  $("usernameText").textContent = `Tên đăng nhập: ${user.username || ""}`;
  $("emailText").textContent = `Email: ${user.email || ""}`;
  $("phoneText").textContent = `SĐT: ${user.phone || ""}`;
}
function toSubjectsString(val) {
  if (Array.isArray(val)) return val.join(", ");
  return (val || "").toString();
}
function toSubjectsArray(val) {
  return (val || "")
    .split(",")
    .map(s => s.trim())
    .filter(Boolean);
}
function fillForm(profile) {
  $("avatarUrl").value = profile.avatarUrl || "";
  $("preferenceSubjects").value = toSubjectsString(profile.preferenceSubjects);
  $("budgetMin").value = profile.budgetMin ?? "";
  $("budgetMax").value = profile.budgetMax ?? "";
  $("location").value = profile.location || "";
  $("bio").value = profile.bio || "";
  setAvatar(profile.avatarUrl);
}
function readForm() {
  return {
    avatarUrl: $("avatarUrl").value.trim(),
    preferenceSubjects: toSubjectsArray($("preferenceSubjects").value),
    budgetMin: Number($("budgetMin").value) || 0,
    budgetMax: Number($("budgetMax").value) || 0,
    location: $("location").value.trim(),
    bio: $("bio").value.trim()
  };
}

// Tải hồ sơ học viên: GET /students/me
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
    const res = await fetch(`${API_BASE}/students/me`, {
      headers: { "Authorization": `Bearer ${token}` }
    });

    if (res.status === 401) {
      localStorage.clear();
      location.href = "login.html";
      return;
    }

    let json = {};
    try { json = await res.json(); } catch {}

    const profile = (res.ok && json?.success && json?.data) ? json.data : {
      avatarUrl: user.avatarUrl || "",
      preferenceSubjects: [],
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
      avatarUrl: user.avatarUrl || "",
      preferenceSubjects: [],
      budgetMin: 0,
      budgetMax: 0,
      location: "",
      bio: ""
    };
    fillForm(state.original);
    setEditable(false);
  }

  // Xem trước avatar khi nhập link
  $("avatarUrl").addEventListener("input", (e) => setAvatar(e.target.value));
}

// Modal
function openConfirm() { $("confirmModal").style.display = "flex"; }
function closeConfirm() { $("confirmModal").style.display = "none"; }

// Lưu hồ sơ: PUT /students/me
async function saveProfile() {
  const token = localStorage.getItem("token");
  const body = readForm();

  if (body.budgetMin && body.budgetMax && body.budgetMin > body.budgetMax) {
    alert("Ngân sách tối thiểu không được lớn hơn tối đa.");
    return;
  }

  try {
    const res = await fetch(`${API_BASE}/students/me`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(body)
    });

    if (res.status === 401) {
      localStorage.clear();
      location.href = "login.html";
      return;
    }

    const json = await res.json().catch(() => ({}));
    if (!res.ok || !json?.success) {
      alert(json?.message || "Cập nhật hồ sơ thất bại!");
      return;
    }

    // Đồng bộ lại dữ liệu đã lưu
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

// Sự kiện
document.addEventListener("DOMContentLoaded", () => {
  loadProfile();

  $("editBtn").addEventListener("click", () => {
    fillForm(state.original || {});
    setEditable(true);
  });

  $("saveBtn").addEventListener("click", () => openConfirm());
  $("cancelSave").addEventListener("click", () => {
    closeConfirm();
    fillForm(state.original || {});
    setEditable(false);
  });
  $("confirmSave").addEventListener("click", () => saveProfile());
});
// ...existing code...