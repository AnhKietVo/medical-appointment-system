
// Submit form
document.getElementById('doctorForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const form = e.target;
  const formData = new FormData(form);
  const json = Object.fromEntries(formData.entries());
  json.dateOfBirth = formatDateForAPI(json.dateOfBirth);

  const res = await fetch('/api/admin/add-doctor', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(json)
  });

  const result = await res.json();
  document.getElementById('msg').textContent = result.message;
  if (res.ok) form.reset();
});

