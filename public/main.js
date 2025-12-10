const API_URL = "/api/items";

async function fetchItems() {
  const container = document.getElementById("itemsContainer");
  container.innerHTML = "<p class='empty-message'>Loading...</p>";

  try {
    const res = await fetch(API_URL);
    if (!res.ok) throw new Error("Failed to load items");

    const items = await res.json();
    renderItems(items);
  } catch (err) {
    console.error(err);
    container.innerHTML = "<p class='empty-message'>Could not load items. Please try again.</p>";
  }
}

function renderItems(items) {
  const container = document.getElementById("itemsContainer");

  if (!items || items.length === 0) {
    container.innerHTML = "<p class='empty-message'>No items yet. Be the first to add one!</p>";
    return;
  }

  // Newest first (by id)
  const sorted = [...items].sort((a, b) => b.id - a.id);

  container.innerHTML = "";
  sorted.forEach((item) => {
    const div = document.createElement("article");
    div.className = "item-card";
    div.innerHTML = `
      <div class="item-title-row">
        <span class="item-title">${escapeHtml(item.title)}</span>
        <span class="item-meta">#${item.id} • ${escapeHtml(item.createdAt || "")}</span>
      </div>
      <p class="item-description">${escapeHtml(item.description)}</p>
      <div class="item-tags">
        ${item.place ? `<span class="tag place">📍 ${escapeHtml(item.place)}</span>` : ""}
        ${item.date ? `<span class="tag date">📅 ${escapeHtml(item.date)}</span>` : ""}
        ${item.contact ? `<span class="tag contact">☎ ${escapeHtml(item.contact)}</span>` : ""}
      </div>
    `;
    container.appendChild(div);
  });
}

function escapeHtml(str) {
  if (!str) return "";
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}

async function handleFormSubmit(event) {
  event.preventDefault();
  const form = event.target;
  const messageEl = document.getElementById("formMessage");
  messageEl.textContent = "";
  messageEl.className = "form-message";

  const formData = new FormData(form);
  const title = formData.get("title").trim();
  const description = formData.get("description").trim();

  if (!title || !description) {
    messageEl.textContent = "Please fill the required fields.";
    messageEl.classList.add("error");
    return;
  }

  try {
    const res = await fetch(API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
      },
      body: new URLSearchParams(formData).toString(),
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error(text || "Failed to submit item");
    }

    messageEl.textContent = "Item submitted successfully!";
    messageEl.classList.add("success");
    form.reset();

    // Reload list
    fetchItems();
  } catch (err) {
    console.error(err);
    messageEl.textContent = err.message || "Something went wrong.";
    messageEl.classList.add("error");
  }
}

window.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("itemForm");
  const refreshBtn = document.getElementById("refreshBtn");

  form.addEventListener("submit", handleFormSubmit);
  refreshBtn.addEventListener("click", fetchItems);

  fetchItems();
});
