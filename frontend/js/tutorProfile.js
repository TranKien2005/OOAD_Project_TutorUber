const API_BASE = "http://localhost:8080/api";

const state = { original: null, editable: false };

const $ = (id) => document.getElementById(id);

function setEditable(enabled) {
  state.editable = enabled;
  ["avatar","specialization","hourlyRate","yearsOfExperience","education","bio"].forEach(id => {
    const el = $(id);
    if (el) el.disabled = !enabled;
  });
  $("saveBtn").disabled = !enabled;
}
function setAvatar(url) {
  $("avatarImg").src = url && url.trim() ? url.trim() : "../images/default-avatar.png";
}
function fillTutorHeader(user, profile) {
  $("helloText").textContent = `Xin chào, ${user.fullName || user.username || "Gia sư"}!`;
  $("usernameText").textContent = `Tên đăng nhập: ${user.username || ""}`;
  $("emailText").textContent = `Email: ${user.email || ""}`;
  $("phoneText").textContent = `SĐT: ${user.phone || ""}`;

  const rating = profile?.rating ?? null;
  const total = profile?.totalRatings ?? null;
  $("ratingText").textContent = rating != null ? `Đánh giá: ${rating} (${total || 0} lượt)` : "Đánh giá: N/A";

  const ver = profile?.verificationStatus || "PENDING";
  $("verifyText").textContent = `Xác minh: ${ver}`;
}

function fillForm(profile) {
  $("avatar").value = profile.avatar || "";
  $("specialization").value = profile.specialization || "";
  $("hourlyRate").value = profile.hourlyRate ?? "";
  $("yearsOfExperience").value = profile.yearsOfExperience ?? "";
  $("education").value = profile.education || "";
  $("bio").value = profile.bio || "";
  setAvatar(profile.avatar);
}

function readForm() {
  return {
    avatar: $("avatar").value.trim(),
    specialization: $("specialization").value.trim(),
    hourlyRate: Number($("hourlyRate").value) || 0,
    yearsOfExperience: Number($("yearsOfExperience").value) || 0,
    education: $("education").value.trim(),
    bio: $("bio").value.trim()
  };
}

// GET /tutors/{tutorId}/profile
async function loadProfile() {
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const role = (localStorage.getItem("role") || "").toUpperCase();
  const tutorId = user?.id;

  if (!tutorId || role !== "tUTOR".toUpperCase()) {
    location.href = "login.html";
    return;
  }

  try {
    const res = await fetch(`${API_BASE}/tutors/${tutorId}/profile`, { method: "GET" });
    let json = {};
    try { json = await res.json(); } catch {}

    const profile = (res.ok && json?.success && json?.data) ? json.data : {
      avatar: user.avatar || "",
      bio: "",
      hourlyRate: 0,
      rating: null,
      totalRatings: 0,
      verificationStatus: "PENDING",
      specialization: "",
      education: "",
      yearsOfExperience: 0
    };

    state.original = profile;
    fillForm(profile);
    fillTutorHeader(user, profile);
    setEditable(false);
  } catch (e) {
    console.error("Load tutor profile error:", e);
    const fallback = {
      avatar: user.avatar || "",
      bio: "",
      hourlyRate: 0,
      rating: null,
      totalRatings: 0,
      verificationStatus: "PENDING",
      specialization: "",
      education: "",
      yearsOfExperience: 0
    };
    state.original = fallback;
    fillForm(fallback);
    fillTutorHeader(user, fallback);
    setEditable(false);
  }

  // Xem trước avatar khi nhập link
  $("avatar").addEventListener("input", (e) => setAvatar(e.target.value));
}

// Modal
function openConfirm() { $("confirmModal").style.display = "flex"; }
function closeConfirm() { $("confirmModal").style.display = "none"; }

// PUT /tutors/{tutorId}/profile
async function saveProfile() {
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const tutorId = user?.id;
  const body = readForm();

  if (body.hourlyRate < 0 || body.yearsOfExperience < 0) {
    alert("Đơn giá/giờ và số năm kinh nghiệm phải không âm.");
    return;
  }

  try {
    const res = await fetch(`${API_BASE}/tutors/${tutorId}/profile`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });
    const json = await res.json().catch(() => ({}));

    if (!res.ok || !json?.success) {
      alert(json?.message || "Cập nhật hồ sơ thất bại!");
      return;
    }

    state.original = json.data || body;
    fillForm(state.original);
    // rating/verification có thể đổi sau cập nhật (nếu backend trả về)
    fillTutorHeader(user, state.original);
    setEditable(false);
    alert("Đã cập nhật hồ sơ thành công!");
  } catch (e) {
    console.error("Save tutor profile error:", e);
    alert("Lỗi kết nối khi lưu hồ sơ!");
  } finally {
    closeConfirm();
  }
}

// Events
document.addEventListener("DOMContentLoaded", () => {
  loadProfile();

  $("editBtn").addEventListener("click", () => {
    fillForm(state.original || {});
    setEditable(true);
  });

  $("saveBtn").addEventListener("click", openConfirm);
  $("cancelSave").addEventListener("click", () => {
    closeConfirm();
    fillForm(state.original || {});
    setEditable(false);
  });
  $("confirmSave").addEventListener("click", saveProfile);
});