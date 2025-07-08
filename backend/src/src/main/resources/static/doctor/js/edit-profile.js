let doctorId = null;

    async function getCurrentDoctorId() {
        try {
            const res = await fetch("/api/doctor/current", { credentials: 'include' });
            const json = await res.json();
            doctorId = json.data.doctor_id;
            console.log("Doctor ID:", doctorId);

            // Gọi hàm load dữ liệu
            loadDoctor(doctorId);
        } catch (err) {
            console.error("Error fetching current doctor:", err);
            document.getElementById("msg").textContent = "Lỗi tải dữ liệu.";
        }
    }

    async function loadDoctor(id) {
        try {
            const res = await fetch(`/api/doctor/${id}`, { credentials: 'include' });
            const json = await res.json();
            const doc = json.data;

            const form = document.forms['editDoctorForm'];
            form.fullName.value = doc.fullName;
            form.dateOfBirth.value = formatDateForInput(doc.dateOfBirth);
            form.qualification.value = doc.qualification;
            form.specialist.value = doc.specialist;
            form.email.value = doc.email;
            form.phone.value = doc.phone;
            form.password.value = doc.password;
        } catch (err) {
            console.error("Error loading doctor:", err);
            document.getElementById("msg").textContent = "Lỗi lấy dữ liệu.";
        }
    }

    document.getElementById("editDoctorForm").addEventListener("submit", async (e) => {
        e.preventDefault();
        const form = e.target;
        const doctorData = Object.fromEntries(new FormData(form).entries());
        doctorData.dateOfBirth = formatDateForAPI(doctorData.dateOfBirth);
        try {
            const res = await fetch(`/api/doctor/update/${doctorId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify(doctorData)
            });

            if (res.ok) {
                document.getElementById("msg").textContent = "Cập nhật thành công!";
            } else {
                document.getElementById("msg").textContent = "Cập nhật thất bại!";
            }
        } catch (err) {
            console.error("Update failed", err);
            document.getElementById("msg").textContent = "Xảy ra lỗi không thể cập nhật.";
        }
    });

window.addEventListener('DOMContentLoaded', getCurrentDoctorId);