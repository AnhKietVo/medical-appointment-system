async function onRegisterSubmit(e) {
      e.preventDefault();
      const fullName = e.target.fullName.value;
      const email = e.target.email.value;
      const password = e.target.password.value;

      const res = await fetch('/api/user/register', {
        method: 'POST',
        headers: {'Content-Type':'application/json'},
        body: JSON.stringify({fullName,email, password})
      });
      const data = await res.json();

      alert(data.message);
    }
document.getElementById('RegisterForm').addEventListener('submit', onRegisterSubmit);