const urlParams = new URLSearchParams(window.location.search);
  const id = urlParams.get("id");

  async function loadAppointment() {
    const res = await fetch(`/api/doctor/appointment/${id}`, { credentials: "include" });
    const json = await res.json();
    const data = json.data;
    const form = document.forms['commentForm'];
    form.id.value = data.id;
    form.fullName.value = data.fullname;
    form.age.value = data.age;
    form.phone.value = data.phone;
    form.diseases.value = data.diseases;
  }

  document.getElementById("commentForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const obj = Object.fromEntries(formData.entries());

    console.log(obj); // nên log ra: { id: "1", comment: "Bệnh nhân cần nghỉ ngơi" }

    const res = await fetch("/api/doctor/appointment/comment", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(obj)
    });

    const msg = document.getElementById("msg");
    if (res.ok) {
      msg.textContent = "Thêm chuẩn đoán thành công!";
      msg.classList.remove("text-danger");
      msg.classList.add("text-success");
    } else {
      msg.textContent = "Thêm chuẩn đoán thất bại!";
    }
  });

  window.addEventListener("DOMContentLoaded", loadAppointment);