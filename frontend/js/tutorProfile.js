"use strict";

const API_BASE = "http://localhost:8080/api";
const AUTH_BASE = `${API_BASE}/auth`;

const state = { original: null, editable: false };
const $ = (id) => document.getElementById(id);

function setText(id, text) {
  const el = $(id);
  if (el) el.textContent = text;
}
function setDisplay(id, value) {
  const el = $(id);
  if (el) el.style.display = value;
}

function setEditable(enabled) {
  state.editable = enabled;
  const ids = ["avatar","specialization","hourlyRate","yearsOfExperience","education","bio"];
  for (const id of ids) {
    const el = $(id);
    if (el) el.disabled = !enabled;
  }
  const saveBtn = $("saveBtn");
  if (saveBtn) saveBtn.disabled = !enabled;
}

function setAvatar(url) {
  const img = $("avatarImg");
  if (img) img.src = url && url.trim() ? url.trim() : "../images/default-avatar.png";
}

function fillHeader(user, profile) {
  setText("helloText", `Xin chào, ${user.fullName || user.username || "Gia sư"}!`);
  setText("usernameText", `Tên đăng nhập: ${user.username || ""}`);
  setText("emailText", `Email: ${user.email || ""}`);
  setText("phoneText", `SĐT: ${user.phone || ""}`);

  const rating = profile && profile.rating != null ? profile.rating : null;
  const total = profile && profile.totalRatings != null ? profile.totalRatings : 0;
  setText("ratingText", rating != null ? `Đánh giá: ${rating} (${total} lượt)` : "Đánh giá: N/A");

  const ver = (profile && profile.verificationStatus) || "PENDING";
  setText("verifyText", `Xác minh: ${ver}`);
}

function fillForm(profile) {
  const avatarEl = $("avatar");
  if (avatarEl) avatarEl.value = profile.avatar || "";

  const specEl = $("specialization");
  if (specEl) specEl.value = profile.specialization || "";

  const rateEl = $("hourlyRate");
  if (rateEl) rateEl.value = profile.hourlyRate != null ? profile.hourlyRate : "";

  const expEl = $("yearsOfExperience");
  if (expEl) expEl.value = profile.yearsOfExperience != null ? profile.yearsOfExperience : "";

  const eduEl = $("education");
  if (eduEl) eduEl.value = profile.education || "";

  const bioEl = $("bio");
  if (bioEl) bioEl.value = profile.bio || "";

  setAvatar(profile.avatar || "");
}

function readForm() {
  const toNumberOrNull = (id) => {
    const v = ($(id)?.value || "").trim();
    if (v === "") return null;
    const n = Number(v);
    return Number.isNaN(n) ? null : n;
  };
  return {
    avatar: ( $("avatar")?.value || "" ).trim(),
    specialization: ( $("specialization")?.value || "" ).trim(),
    hourlyRate: toNumberOrNull("hourlyRate"),
    yearsOfExperience: toNumberOrNull("yearsOfExperience"),
    education: ( $("education")?.value || "" ).trim(),
    bio: ( $("bio")?.value || "" ).trim()
  };
}

// GET /api/tutors/{id}/profile
async function loadProfile() {
  const token = localStorage.getItem("token");
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const role = (localStorage.getItem("role") || "").toUpperCase();

  if (!user?.id || role !== "TUTOR") {
    location.href = "login.html";
    return;
  }

  try {
    const res = await fetch(`${API_BASE}/tutors/${user.id}/profile`, {
      method: "GET",
      headers: { ...(token ? { "Authorization": `Bearer ${token}` } : {}) }
    });

    if (res.status === 401) {
      localStorage.clear();
      location.href = "login.html";
      return;
    }

    const text = await res.text();
    let json = {};
    try { json = JSON.parse(text); } catch {}

    const profile = (res.ok && json?.data) ? json.data : {
      avatar: user.avatar || "",
      bio: "",
      hourlyRate: null,
      rating: null,
      totalRatings: 0,
      verificationStatus: "PENDING",
      specialization: "",
      education: "",
      yearsOfExperience: null,
      user: { fullName: user.fullName || "", email: user.email || "", phone: user.phone || "" }
    };

    state.original = profile;
    fillForm(profile);
    fillHeader(user, profile);
    setEditable(false);
  } catch (e) {
    console.error("Load tutor profile error:", e);
    const fallback = {
      avatar: user.avatar || "",
      bio: "",
      hourlyRate: null,
      rating: null,
      totalRatings: 0,
      verificationStatus: "PENDING",
      specialization: "",
      education: "",
      yearsOfExperience: null,
      user: { fullName: user.fullName || "", email: user.email || "", phone: user.phone || "" }
    };
    state.original = fallback;
    fillForm(fallback);
    fillHeader(user, fallback);
    setEditable(false);
  }

  const avatarInput = $("avatar");
  if (avatarInput) {
    avatarInput.addEventListener("input", (e) => setAvatar(e.target.value));
  }
}

// Modal
function openConfirm() { setDisplay("confirmModal", "flex"); }
function closeConfirm() { setDisplay("confirmModal", "none"); }

// PUT /api/tutors/{id}/profile (UpdateProfileRequest)
async function saveProfile() {
  const token = localStorage.getItem("token");
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const body = readForm();

  if (body.hourlyRate != null && body.hourlyRate < 0) {
    alert("Đơn giá/giờ phải không âm.");
    return;
  }
  if (body.yearsOfExperience != null && body.yearsOfExperience < 0) {
    alert("Số năm kinh nghiệm phải không âm.");
    return;
  }

  try {
    const res = await fetch(`${API_BASE}/tutors/${user.id}/profile`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        ...(token ? { "Authorization": `Bearer ${token}` } : {})
      },
      body: JSON.stringify(body)
    });

    const text = await res.text();
    let json = {};
    try { json = JSON.parse(text); } catch {}

    if (!res.ok) {
      console.warn("Update tutor profile error:", res.status, text);
      alert(json?.message || "Cập nhật hồ sơ thất bại!");
      return;
    }

    state.original = json.data || body;
    fillForm(state.original);
    fillHeader(user, state.original);
    setEditable(false);
    alert("Đã cập nhật hồ sơ thành công!");
  } catch (e) {
    console.error("Save tutor profile error:", e);
    alert("Lỗi kết nối khi lưu hồ sơ!");
  } finally {
    closeConfirm();
  }
}

// Đổi mật khẩu
function openChangePwd() {
  setText("changePwdMessage", "");
  const oldEl = $("oldPassword"); if (oldEl) oldEl.value = "";
  const newEl = $("newPassword"); if (newEl) newEl.value = "";
  const confEl = $("confirmPassword"); if (confEl) confEl.value = "";
  setDisplay("changePwdPanel", "flex");
}
function closeChangePwd() {
  setDisplay("changePwdPanel", "none");
}

async function changePassword() {
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const token = localStorage.getItem("token");
  const userId = user?.id;

  const currentPassword = (($("oldPassword") && $("oldPassword").value) || "").trim();
  const newPassword = (($("newPassword") && $("newPassword").value) || "").trim();
  const confirmPassword = (($("confirmPassword") && $("confirmPassword").value) || "").trim();

  if (!currentPassword || !newPassword || !confirmPassword) {
    setText("changePwdMessage", "Vui lòng nhập đầy đủ mật khẩu cũ, mới và xác nhận.");
    return;
  }
  if (newPassword.length < 6) {
    setText("changePwdMessage", "Mật khẩu mới phải từ 6 ký tự trở lên.");
    return;
  }
  if (newPassword !== confirmPassword) {
    setText("changePwdMessage", "Xác nhận mật khẩu không khớp.");
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
    try { json = JSON.parse(text); } catch {}

    if (!res.ok) {
      const msg = json?.message || (res.status === 400 ? "Dữ liệu không hợp lệ hoặc mật khẩu cũ sai." : `Đổi mật khẩu thất bại! (${res.status})`);
      setText("changePwdMessage", msg);
      return;
    }

    alert("Đổi mật khẩu thành công!");
    closeChangePwd();
  } catch (e) {
    console.error("Change password error:", e);
    setText("changePwdMessage", "Lỗi kết nối. Vui lòng thử lại.");
  }
}

// Events
document.addEventListener("DOMContentLoaded", () => {
  loadProfile();

  const editBtn = $("editBtn");
  if (editBtn) {
    editBtn.addEventListener("click", () => {
      fillForm(state.original || {});
      setEditable(true);
    });
  }

  const saveBtn = $("saveBtn");
  if (saveBtn) saveBtn.addEventListener("click", openConfirm);

  const cancelSave = $("cancelSave");
  if (cancelSave) {
    cancelSave.addEventListener("click", () => {
      closeConfirm();
      fillForm(state.original || {});
      setEditable(false);
    });
  }

  const confirmSave = $("confirmSave");
  if (confirmSave) confirmSave.addEventListener("click", () => saveProfile());

  const changePwdBtn = $("changePwdBtn");
  if (changePwdBtn) changePwdBtn.addEventListener("click", openChangePwd);

  const cancelChangePwd = $("cancelChangePwd");
  if (cancelChangePwd) cancelChangePwd.addEventListener("click", closeChangePwd);

  const confirmChangePwd = $("confirmChangePwd");
  if (confirmChangePwd) confirmChangePwd.addEventListener("click", changePassword);
});