"use strict";

const API_BASE = "http://localhost:8080/api";
const defaultAvatar = "../images/default-avatar.png";

const $ = (id) => document.getElementById(id);

function normalizeSubject(val) {
  const s = (val || "").trim().toLowerCase();
  const map = {
    math: "Mathematics",
    mathematic: "Mathematics",
    mathemectics: "Mathematics",
    mathematics: "Mathematics",
    "toán": "Mathematics",
    physics: "Physics",
    chemistry: "Chemistry",
    english: "English",
    biology: "Biology",
    literature: "Literature",
  };
  return map[s] || "";
}

function validateFilters({ minRate, maxRate, minRating }) {
  if (minRate != null && maxRate != null && Number(minRate) > Number(maxRate)) {
    alert("minRate không được lớn hơn maxRate.");
    return false;
  }
  if (minRating != null && (Number(minRating) < 0 || Number(minRating) > 5)) {
    alert("minRating phải trong khoảng 0..5.");
    return false;
  }
  return true;
}

function tutorCard(t) {
  const avatar = t.avatar || defaultAvatar;
  const name = t.fullName || t.username || "Gia sư";
  const spec = t.specialization || "Chưa cập nhật";
  const edu = t.education || "";
  const rate = typeof t.hourlyRate === "number" ? `${t.hourlyRate.toLocaleString()} đ/giờ` : "N/A";
  const rating = typeof t.rating === "number" ? `${t.rating} ★` : "N/A";
  const totalRatings = typeof t.totalRatings === "number" ? `(${t.totalRatings} lượt)` : "";
  const years = typeof t.yearsOfExperience === "number" ? `${t.yearsOfExperience} năm` : "N/A";
  const verified = (t.verificationStatus || "").toUpperCase() === "APPROVED" ? "Đã xác minh" : "Chưa xác minh";
  const bio = t.bio || "";

  // tutorId CHÍNH XÁC từ object gia sư
  const tutorId = t.id;

  return `
    <div class="tutor-card">
      <div class="thumb">
        <img src="${avatar}" alt="avatar" onerror="this.src='${defaultAvatar}'">
      </div>
      <div class="body">
        <div class="tutor-name">${name}</div>
        <div class="tutor-meta">
          <div>Chuyên môn: <b>${spec}</b></div>
          ${edu ? `<div>Học vấn: ${edu}</div>` : ""}
          <div>Đơn giá: <b>${rate}</b></div>
          <div>Đánh giá: <b>${rating}</b> ${totalRatings}</div>
          <div>Kinh nghiệm: ${years}</div>
          <div>Trạng thái: ${verified}</div>
          ${bio ? `<div>Giới thiệu: ${bio}</div>` : ""}
        </div>
        <div class="card-actions">
          <button class="card-btn primary" onclick="viewTutorClasses(${tutorId})">Xem chi tiết lớp học</button>
        </div>
      </div>
    </div>
  `;
}

function viewTutorClasses(tutorId) {
  if (!Number.isFinite(Number(tutorId))) return;
  window.location.href = `./tutorClasses.html?tutorId=${encodeURIComponent(tutorId)}`;
}

function renderList(items) {
  const grid = $("cardsGrid");
  const info = $("resultInfo");
  if (!grid || !info) return;

  if (!items || items.length === 0) {
    grid.innerHTML = `<div class="empty-state">Không tìm thấy gia sư phù hợp. Hãy điều chỉnh bộ lọc.</div>`;
    info.textContent = "0 kết quả";
    return;
  }

  grid.innerHTML = items.map(tutorCard).join("");
  const subjectText = $("subject")?.value || "Tất cả môn";
  info.textContent = `${items.length} kết quả • Môn: ${subjectText}`;
}

function collectFilters() {
  const subjectRaw = $("subject")?.value || "";
  const subject = normalizeSubject(subjectRaw);

  const minRateStr = $("minRate")?.value || "";
  const maxRateStr = $("maxRate")?.value || "";
  const minRatingStr = $("minRating")?.value || "";
  const verifiedChecked = !!($("verified") && $("verified").checked);

  const filters = {};
  if (subject) filters.subject = subject;
  if (minRateStr) filters.minRate = Number(minRateStr);
  if (maxRateStr) filters.maxRate = Number(maxRateStr);
  if (minRatingStr) filters.minRating = Number(minRatingStr);
  if (verifiedChecked) filters.verified = true;

  return filters;
}

async function searchTutors() {
  try {
    const filters = collectFilters();

    if (
      filters.subject == null &&
      filters.minRate == null &&
      filters.maxRate == null &&
      filters.minRating == null &&
      filters.verified == null
    ) {
      const ok = confirm("Bạn chưa chọn tiêu chí lọc. Tiếp tục sẽ trả về tất cả gia sư. Tiếp tục?");
      if (!ok) return;
    }

    if (!validateFilters(filters)) return;

    const res = await fetch(`${API_BASE}/tutors/search`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(filters),
    });

    let json = {};
    try { json = await res.json(); } catch { }

    if (!res.ok || !json?.success) {
      alert(json?.message || "Tìm kiếm thất bại!");
      renderList([]);
      return;
    }

    const list = Array.isArray(json.data) ? json.data : [];

    // Lọc dự phòng nếu backend chưa áp dụng filter
    let finalList = list;

    if (filters.subject) {
      const s = filters.subject.toLowerCase();
      finalList = finalList.filter(t =>
        (t.specialization || "").toLowerCase().includes(s)
      );
    }
    if (filters.verified) {
      finalList = finalList.filter(
        t => (t.verificationStatus || "").toUpperCase() === "APPROVED"
      );
    }
    if (filters.minRate != null) {
      finalList = finalList.filter(
        t => typeof t.hourlyRate === "number" && t.hourlyRate >= filters.minRate
      );
    }
    if (filters.maxRate != null) {
      finalList = finalList.filter(
        t => typeof t.hourlyRate === "number" && t.hourlyRate <= filters.maxRate
      );
    }
    if (filters.minRating != null) {
      finalList = finalList.filter(
        t => typeof t.rating === "number" && t.rating >= filters.minRating
      );
    }

    renderList(finalList);
  } catch (e) {
    console.error("Search error:", e);
    alert("Lỗi kết nối khi tìm kiếm!");
    renderList([]);
  }
}

function resetFilters() {
  try {
    if ($("subject")) $("subject").value = "";
    if ($("minRate")) $("minRate").value = "";
    if ($("maxRate")) $("maxRate").value = "";
    if ($("minRating")) $("minRating").value = "";
    if ($("verified")) $("verified").checked = false;
    if ($("resultInfo")) $("resultInfo").textContent = "";
    if ($("cardsGrid")) $("cardsGrid").innerHTML = "";
  } catch (e) {
    console.error("Reset filters error:", e);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  $("searchBtn")?.addEventListener("click", searchTutors);
  $("resetBtn")?.addEventListener("click", resetFilters);

  ["subject", "minRate", "maxRate", "minRating"].forEach(id => {
    const el = $(id);
    if (!el) return;
    el.addEventListener("keydown", (e) => {
      if (e.key === "Enter") searchTutors();
    });
  });
});