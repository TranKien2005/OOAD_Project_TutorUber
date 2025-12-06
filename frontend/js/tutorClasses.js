"use strict";

const API_BASE = "http://localhost:8080/api";
const $ = (id) => document.getElementById(id);

function getTutorIdFromQuery() {
  const p = new URLSearchParams(window.location.search);
  const id = Number(p.get("tutorId"));
  return Number.isFinite(id) && id > 0 ? id : null;
}

function vnd(n) {
  return typeof n === "number" ? `${n.toLocaleString()} đ` : "N/A";
}

function classCard(c, tutorId) {
  const status = c.status || "ACTIVE";
  const schedules = Array.isArray(c.schedules) ? c.schedules : [];
  const scheduleHtml = schedules.map(s =>
    `<span class="badge">${(s.dayOfWeek || "").toString()} ${s.startTime || ""}-${s.endTime || ""}</span>`
  ).join("");

  return `
    <div class="class-card">
      <div class="class-header">
        <div class="class-title">${c.subject || "Lớp học"}</div>
        <div class="muted">Mã lớp: ${c.id ?? "N/A"}</div>
      </div>
      <div class="class-meta">
        <div>Gia sư ID: <b>${tutorId}</b>${c.tutorName ? ` • Tên: ${c.tutorName}` : ""}</div>
        <div>Học phí/buổi: <b>${vnd(c.fee)}</b></div>
        <div>Trạng thái: <b>${status}</b></div>
        <div>Địa điểm: ${c.location || "N/A"}</div>
        <div>Số học viên: ${c.currentStudents ?? 0}/${c.maxStudents ?? "N/A"}</div>
        ${c.description ? `<div>Mô tả: ${c.description}</div>` : ""}
      </div>
      <div class="class-badges">${scheduleHtml}</div>
      <div class="card-actions">
        <button class="btn-primary" onclick="openBookingModal(${c.id})">Đặt lớp học</button>
        <button class="btn-secondary" onclick="history.back()">Quay lại</button>
      </div>
    </div>
  `;
}

function renderClasses(list, tutorId) {
  const grid = $("classesGrid");
  const meta = $("metaInfo");
  if (!grid || !meta) return;

  if (!list || list.length === 0) {
    grid.innerHTML = `<div class="empty-state">Gia sư chưa có lớp nào mở.</div>`;
    meta.textContent = `Tutor ID: ${tutorId} • 0 lớp`;
    return;
  }
  grid.innerHTML = list.map(c => classCard(c, tutorId)).join("");
  meta.textContent = `Tutor ID: ${tutorId} • ${list.length} lớp`;
}

// Booking modal
let bookingClassId = null;
function openBookingModal(classId) {
  bookingClassId = classId;
  $("confirmText").textContent = `Bạn có chắc muốn đặt lớp #${classId}?`;
  $("confirmModal").style.display = "flex";
}
function closeBookingModal() {
  $("confirmModal").style.display = "none";
  bookingClassId = null;
}

async function placeBooking() {
  try {
    const user = JSON.parse(localStorage.getItem("user") || "{}");
    const role = (localStorage.getItem("role") || "").toUpperCase();
    const studentId = user?.id;

    if (!studentId || role !== "STUDENT") {
      alert("Vui lòng đăng nhập bằng tài khoản Học viên để đặt lớp.");
      window.location.href = "./login.html";
      return;
    }
    if (!bookingClassId) {
      alert("Thiếu mã lớp học.");
      return;
    }

    const body = { studentId, classId: bookingClassId };
    const res = await fetch(`${API_BASE}/bookings`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });
    const json = await res.json().catch(() => ({}));
    if (!res.ok || !json?.success) {
      alert(json?.message || "Đặt lớp thất bại!");
      return;
    }
    alert("Đặt lớp thành công!");
    closeBookingModal();
  } catch (e) {
    console.error("Booking error:", e);
    alert("Lỗi kết nối khi đặt lớp!");
  }
}

async function loadTutorClasses() {
  const tutorId = getTutorIdFromQuery();
  if (!tutorId) {
    alert("Thiếu tutorId. Quay lại trang tìm kiếm.");
    history.back();
    return;
  }

  try {
    const res = await fetch(`${API_BASE}/classes/tutor/${tutorId}`, { method: "GET" });
    let json = {};
    try { json = await res.json(); } catch {}

    if (!res.ok || !json?.success) {
      alert(json?.message || "Không tải được danh sách lớp.");
      renderClasses([], tutorId);
      return;
    }

    const list = Array.isArray(json.data) ? json.data : [];
    renderClasses(list, tutorId);
  } catch (e) {
    console.error("Load classes error:", e);
    alert("Lỗi kết nối khi tải lớp.");
    renderClasses([], tutorId);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  $("backBtn")?.addEventListener("click", () => history.back());
  $("cancelBooking")?.addEventListener("click", closeBookingModal);
  $("confirmBooking")?.addEventListener("click", placeBooking);
  loadTutorClasses();
});