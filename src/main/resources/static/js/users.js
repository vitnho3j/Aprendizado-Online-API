import config from "./config.js";
const usersList = document.getElementById('users');
const tr_principal = document.getElementById("tr-principal");
const option_active = document.getElementById("option-active");
const option_inative = document.getElementById("options-inative-container");

var userClicked 

function loadUsers() {
    while (usersList.firstChild) {
        usersList.removeChild(usersList.firstChild);
    }
    while (tr_principal.firstChild) {
        tr_principal.removeChild(tr_principal.firstChild);
    }
    option_active.textContent = "Ativos"
    option_inative.style.display = "grid"

    const thId = document.createElement("th");
    const thName = document.createElement("th");
    const thEmail = document.createElement("th");

    thId.textContent = "Id";
    thName.textContent = "Nome";
    thEmail.textContent = "Email";
    tr_principal.appendChild(thId);
    tr_principal.appendChild(thName);
    tr_principal.appendChild(thEmail);

    fetch(`${config.baseURL}/users`)
        .then(response => response.json())
        .then(data => {
            data.filter(user => user.active === true).forEach(user =>{
                // Criando a estrutura HTML para cada usuário
                const tr = document.createElement('tr');

                const tdId = document.createElement('td');
                tdId.textContent = user.id;
                tr.appendChild(tdId);

                // Coluna com o nome do usuário
                const tdName = document.createElement('td');
                tdName.textContent = user.name;
                tr.appendChild(tdName);

                // Coluna com o email do usuário
                const tdEmail = document.createElement('td');
                tdEmail.textContent = user.email;
                tr.appendChild(tdEmail);

                // Coluna com os ícones de ação (editar e deletar)
                const tdActions = document.createElement('td');

                // Ícone de Editar
                const editLink = document.createElement('a');
                editLink.href = '#editEmployeeModal';
                editLink.classList.add('edit');
                editLink.dataset.toggle = 'modal';
                editLink.addEventListener('click', () => setUserClicked(user.id)); // Adiciona evento de clique
                const editIcon = document.createElement('i');
                editIcon.classList.add('material-icons');
                editIcon.dataset.toggle = 'tooltip';
                editIcon.title = 'Edit';
                editIcon.textContent = '\u{E254}'; // Unicode para o ícone
                editLink.appendChild(editIcon);
                tdActions.appendChild(editLink);

                // Ícone de Deletar
                const deleteLink = document.createElement('a');
                deleteLink.href = '#deleteEmployeeModal';
                deleteLink.classList.add('delete');
                deleteLink.dataset.toggle = 'modal';
                deleteLink.addEventListener('click', () => setUserClicked(user.id)); // Adiciona evento de clique
                const deleteIcon = document.createElement('i');
                deleteIcon.classList.add('material-icons');
                deleteIcon.dataset.toggle = 'tooltip';
                deleteIcon.title = 'Delete';
                deleteIcon.textContent = '\u{E872}'; // Unicode para o ícone
                deleteLink.appendChild(deleteIcon);
                tdActions.appendChild(deleteLink);

                const buyLink = document.createElement('a');
                buyLink.href = '#buyEmployeeModal';
                buyLink.classList.add('buy');
                buyLink.dataset.toggle = 'modal';
                buyLink.addEventListener('click', () => loadPurchases(user.id)); // Adiciona evento de clique
                const buyIcon = document.createElement('i');
                buyIcon.classList.add('material-icons');
                buyIcon.dataset.toggle = 'tooltip';
                buyIcon.title = 'Buy';
                buyIcon.textContent = '\u{E8CC}'; // Unicode para o ícone de compras
                buyLink.appendChild(buyIcon);
                tdActions.appendChild(buyLink);

                // Ícone de Vendas
                const sellLink = document.createElement('a');
                sellLink.href = '#sellEmployeeModal';
                sellLink.classList.add('sell');
                sellLink.dataset.toggle = 'modal';
                sellLink.addEventListener('click', () => loadSales(user.id)); // Adiciona evento de clique
                const sellIcon = document.createElement('i');
                sellIcon.classList.add('material-icons');
                sellIcon.dataset.toggle = 'tooltip';
                sellIcon.title = 'Sell';
                sellIcon.textContent = '\u{E227}'; // Unicode para o ícone de vendas
                sellLink.appendChild(sellIcon);
                tdActions.appendChild(sellLink);

                // Ícone de Cursos
                const courseLink = document.createElement('a');
                courseLink.href = '#courseEmployeeModal';
                courseLink.classList.add('course');
                courseLink.dataset.toggle = 'modal';
                courseLink.addEventListener('click', () => loadCourses(user.id)); // Adiciona evento de clique
                const courseIcon = document.createElement('i');
                courseIcon.classList.add('material-icons');
                courseIcon.dataset.toggle = 'tooltip';
                courseIcon.title = 'Course';
                courseIcon.textContent = '\u{E80C}'; // Unicode para o ícone de cursos
                courseLink.appendChild(courseIcon);
                tdActions.appendChild(courseLink);

                tr.appendChild(tdActions);

                // Adicionando a linha (tr) à lista de usuários
                usersList.appendChild(tr);
            });
        })
        .catch(error => {
            const errorTr = document.createElement('tr');
            const errorTd = document.createElement('td');
            errorTd.colSpan = '4'; // Colspan para ocupar todas as colunas
            errorTd.textContent = `Erro ao carregar usuários: ${error.message}`;
            errorTr.appendChild(errorTd);
            usersList.appendChild(errorTr);
        });
}

function loadCourses(userId) {
    while (usersList.firstChild) {
        usersList.removeChild(usersList.firstChild);
    }
    console.log(`${config.baseURL}/courses/user/${userId}`);
    fetch(`${config.baseURL}/courses/user/${userId}`)
        .then(response => response.json())
        .then(courses => {
            // Limpa o tr_principal antes de adicionar novos cabeçalhos
            while (tr_principal.firstChild) {
                tr_principal.removeChild(tr_principal.firstChild);
            }

            if (!courses || courses.length === 0) {
                const h3 = document.createElement("h3");
                h3.textContent = "Não há cursos";
                tr_principal.appendChild(h3);
            } else {       
            // Adiciona novos cabeçalhos
            const thId = document.createElement("th");
            const thName = document.createElement("th");
            const thDescription = document.createElement("th");
            const thActive = document.createElement("th");
            const thPrice = document.createElement("th");
            const thCategory = document.createElement("th");
            thId.textContent = "Id";
            thName.textContent = "Nome";
            thDescription.textContent = "Description";
            thActive.textContent = "Ativo";
            thPrice.textContent = "Price";
            thCategory.textContent = "Categoria";
            tr_principal.appendChild(thId);
            tr_principal.appendChild(thName);
            tr_principal.appendChild(thDescription);
            tr_principal.appendChild(thActive);
            tr_principal.appendChild(thPrice);
            tr_principal.appendChild(thCategory);

            courses.forEach(sale => {
                // Criando a estrutura HTML para cada compra
                const tr = document.createElement('tr');

                // Coluna com o ID da compra
                const tdId = document.createElement('td');
                tdId.textContent = sale.id;
                tr.appendChild(tdId);

                const tdName = document.createElement('td');
                tdName.textContent = sale.name;
                tr.appendChild(tdName);

                // Coluna com o valor da compra
                const tdDescription = document.createElement('td');
                tdDescription.textContent = sale.description;
                tr.appendChild(tdDescription);

                // Coluna com o ID do curso
                const tdActive = document.createElement('td');
                tdActive.textContent = sale.active;
                tr.appendChild(tdActive);

                const tdPrice = document.createElement('td');
                tdPrice.textContent = sale.price;
                tr.appendChild(tdPrice);

                const tdCategory = document.createElement('td');
                tdCategory.textContent = sale.category;
                tr.appendChild(tdCategory);

                // Coluna com os ícones de ação (editar e deletar)
                const tdActions = document.createElement('td');
                // Adicione seus ícones de ação aqui, se necessário
                tr.appendChild(tdActions);

                // Adicionando a linha (tr) à lista de usuários
                usersList.appendChild(tr);
                
            });
        }
        option_active.textContent = "Voltar"
        option_inative.style.display = "none"
    })
        .catch(error => {
            const errorTr = document.createElement('tr');
            const errorTd = document.createElement('td');
            errorTd.colSpan = '4'; // Colspan para ocupar todas as colunas
            errorTd.textContent = `Erro ao carregar usuários: ${error.message}`;
            errorTr.appendChild(errorTd);
            usersList.appendChild(errorTr);
        });
}

function loadSales(userId) {
    while (usersList.firstChild) {
        usersList.removeChild(usersList.firstChild);
    }
    console.log(`${config.baseURL}/sales/user/${userId}`);
    fetch(`${config.baseURL}/sales/user/${userId}`)
        .then(response => response.json())
        .then(sales => {
            // Limpa o tr_principal antes de adicionar novos cabeçalhos
            while (tr_principal.firstChild) {
                tr_principal.removeChild(tr_principal.firstChild);
            }

            if (!sales || sales.length === 0) {
                const h3 = document.createElement("h3");
                h3.textContent = "Não há vendas";
                tr_principal.appendChild(h3);
            } else {       
            // Adiciona novos cabeçalhos
            const thId = document.createElement("th");
            const thDate = document.createElement("th");
            const thValue = document.createElement("th");
            const thCourse = document.createElement("th");
            thDate.textContent = "Data";
            thValue.textContent = "Valor";
            thCourse.textContent = "Curso";
            thId.textContent = "Id";
            tr_principal.appendChild(thId);
            tr_principal.appendChild(thDate);
            tr_principal.appendChild(thValue);
            tr_principal.appendChild(thCourse);

            sales.forEach(sale => {
                // Criando a estrutura HTML para cada compra
                const tr = document.createElement('tr');

                // Coluna com o ID da compra
                const tdId = document.createElement('td');
                tdId.textContent = sale.id;
                tr.appendChild(tdId);

                // Coluna com o timestamp da compra
                const tdTimestamp = document.createElement('td');
                
                // Criar um objeto Date
                const date = new Date(sale.timestamp[2], sale.timestamp[1], sale.timestamp[0], sale.timestamp[3], sale.timestamp[4], sale.timestamp[5]);
                tdTimestamp.textContent = date;
                tr.appendChild(tdTimestamp);

                // Coluna com o valor da compra
                const tdValue = document.createElement('td');
                tdValue.textContent = sale.value;
                tr.appendChild(tdValue);

                // Coluna com o ID do curso
                const tdCourse = document.createElement('td');
                tdCourse.textContent = sale.course.id;
                tr.appendChild(tdCourse);

                // Coluna com os ícones de ação (editar e deletar)
                const tdActions = document.createElement('td');
                // Adicione seus ícones de ação aqui, se necessário
                tr.appendChild(tdActions);

                option_active.textContent = "Voltar"
                option_inative.style.display = "none"
                // Adicionando a linha (tr) à lista de usuários
                usersList.appendChild(tr);
            });
        }
        option_active.textContent = "Voltar"
        option_inative.style.display = "none"
    })
        .catch(error => {
            const errorTr = document.createElement('tr');
            const errorTd = document.createElement('td');
            errorTd.colSpan = '4'; // Colspan para ocupar todas as colunas
            errorTd.textContent = `Erro ao carregar usuários: ${error.message}`;
            errorTr.appendChild(errorTd);
            usersList.appendChild(errorTr);
        });
}

function loadPurchases(userId) {
    while (usersList.firstChild) {
        usersList.removeChild(usersList.firstChild);
    }
    console.log(`${config.baseURL}/purchases/user/${userId}`);
    fetch(`${config.baseURL}/purchases/user/${userId}`)
        .then(response => response.json())
        .then(purchases => {
            // Limpa o tr_principal antes de adicionar novos cabeçalhos
            while (tr_principal.firstChild) {
                tr_principal.removeChild(tr_principal.firstChild);
            }

            if (!purchases || purchases.length === 0) {
                const h3 = document.createElement("h3");
                h3.textContent = "Não há compras";
                tr_principal.appendChild(h3);
            } else {       
            // Adiciona novos cabeçalhos
            const thId = document.createElement("th");
            const thDate = document.createElement("th");
            const thValue = document.createElement("th");
            const thCourse = document.createElement("th");
            thDate.textContent = "Data";
            thValue.textContent = "Valor";
            thCourse.textContent = "Curso";
            thId.textContent = "Id";
            tr_principal.appendChild(thId);
            tr_principal.appendChild(thDate);
            tr_principal.appendChild(thValue);
            tr_principal.appendChild(thCourse);

            purchases.forEach(purchase => {
                // Criando a estrutura HTML para cada compra
                const tr = document.createElement('tr');

                // Coluna com o ID da compra
                const tdId = document.createElement('td');
                tdId.textContent = purchase.id;
                tr.appendChild(tdId);

                // Coluna com o timestamp da compra
                const tdTimestamp = document.createElement('td');
                
                // Criar um objeto Date
                const date = new Date(purchase.timestamp[2], purchase.timestamp[1], purchase.timestamp[0], purchase.timestamp[3], purchase.timestamp[4], purchase.timestamp[5]);
                tdTimestamp.textContent = date;
                tr.appendChild(tdTimestamp);

                // Coluna com o valor da compra
                const tdValue = document.createElement('td');
                tdValue.textContent = purchase.value;
                tr.appendChild(tdValue);

                // Coluna com o ID do curso
                const tdCourse = document.createElement('td');
                tdCourse.textContent = purchase.course.id;
                tr.appendChild(tdCourse);

                // Coluna com os ícones de ação (editar e deletar)
                const tdActions = document.createElement('td');
                // Adicione seus ícones de ação aqui, se necessário
                tr.appendChild(tdActions);

                option_active.textContent = "Voltar"
                option_inative.style.display = "none"

                // Adicionando a linha (tr) à lista de usuários
                usersList.appendChild(tr);
            });
        }
        option_active.textContent = "Voltar"
        option_inative.style.display = "none"
    })
        .catch(error => {
            const errorTr = document.createElement('tr');
            const errorTd = document.createElement('td');
            errorTd.colSpan = '4'; // Colspan para ocupar todas as colunas
            errorTd.textContent = `Erro ao carregar usuários: ${error.message}`;
            errorTr.appendChild(errorTd);
            usersList.appendChild(errorTr);
        });
}

function loadUsersInative() {
    while (usersList.firstChild) {
        usersList.removeChild(usersList.firstChild);
    }
    while (tr_principal.firstChild) {
        tr_principal.removeChild(tr_principal.firstChild);
    }
    option_active.textContent = "Ativos"
    option_inative.style.display = "grid"

    const thId = document.createElement("th");
    const thName = document.createElement("th");
    const thEmail = document.createElement("th");
    const thDeleteAt = document.createElement("th");

    thId.textContent = "Id";
    thDeleteAt.textContent = "Data de Exclusão";
    thName.textContent = "Nome";
    thEmail.textContent = "Email";
    tr_principal.appendChild(thId);
    tr_principal.appendChild(thDeleteAt);
    tr_principal.appendChild(thName);
    tr_principal.appendChild(thEmail);

    fetch(`${config.baseURL}/users`)
        .then(response => response.json())
        .then(data => {
            data.filter(user => user.active === false).forEach(user =>{
                // Criando a estrutura HTML para cada usuário
                const tr = document.createElement('tr');

                const tdId = document.createElement('td');
                tdId.textContent = user.id;
                tr.appendChild(tdId);

                
                const tdDeletedAt = document.createElement('td');
                const date = new Date(user.deleted_at[2], user.deleted_at[1], user.deleted_at[0], user.deleted_at[3], user.deleted_at[4], user.deleted_at[5]);
                tdDeletedAt.textContent = date;
                tr.appendChild(tdDeletedAt);

                // Coluna com o nome do usuário
                const tdName = document.createElement('td');
                tdName.textContent = user.name;
                tr.appendChild(tdName);

                // Coluna com o email do usuário
                const tdEmail = document.createElement('td');
                tdEmail.textContent = user.email;
                tr.appendChild(tdEmail);

                // Coluna com os ícones de ação (editar e deletar)
                const tdActions = document.createElement('td');

                tr.appendChild(tdActions);

                // Adicionando a linha (tr) à lista de usuários
                usersList.appendChild(tr);
            });
        })
        .catch(error => {
            const errorTr = document.createElement('tr');
            const errorTd = document.createElement('td');
            errorTd.colSpan = '4'; // Colspan para ocupar todas as colunas
            errorTd.textContent = `Erro ao carregar usuários: ${error.message}`;
            errorTr.appendChild(errorTd);
            usersList.appendChild(errorTr);
        });
}

document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('div[onclick="loadUsersInative()"]').addEventListener('click', loadUsersInative);
});

document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('div[onclick="loadUsers()"]').addEventListener('click', loadUsers);
});

function setUserClicked(userId){
    userClicked = userId
}

// Função para deletar usuário
function deleteUser() {
    // alert(`Será excutado na URL: ${config.baseURL}/users/${userClicked}`)
    fetch(`${config.baseURL}/users/${userClicked}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok){
            location.reload();
        } else {
            return response.json().then(error => {
                alert(error.message || "Erro desconhecido");
            })
        }
    }).catch(error => {
        console.error('Erro ao deletar usuário:', error);
    });
}

document.addEventListener('DOMContentLoaded', function() {
    loadUsers();
});

document.getElementById('deleteUserForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evita o envio padrão do formulário

    // Chama a função deleteUser() para deletar o usuário clicado
    deleteUser();
});


document.getElementById('editForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evita o envio padrão do formulário

    const name = document.getElementById('edit-name').value;
    const email = document.getElementById('edit-email').value;

    fetch(`${config.baseURL}/users/${userClicked}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: name,
        email: email
      })
    })
    .then(response => {
      if (response.ok){
        location.reload();
      } else {
        return response.json().then(error => {
            console.log(error)
            alert(error.message || "Erro desconhecido");
        })
      }
    })
    .catch((error) => {
      console.log(error);
    });
  });

document.getElementById('userForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evita o envio padrão do formulário

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;

    fetch(`${config.baseURL}/users`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: name,
        email: email
      })
    })
    .then(response => {
      if (response.ok){
        location.reload();
      } else {
        return response.json().then(error => {
            alert(error.message || "Erro desconhecido");
            console.log(error)
        })
      }
    })
    .catch((error) => {
      console.log(error);
    });
  });
