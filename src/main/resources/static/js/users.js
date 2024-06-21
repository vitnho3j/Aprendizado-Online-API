// import config from "./config.js";
// const usersList = document.getElementById('users');

// document.addEventListener("DOMContentLoaded", function(){
//     fetch(`${config.baseURL}/user`)
//     .then(response => response.json())
//     .then(data => {
//         data.forEach(user => {
//             const listUser = document.createElement('li');
//             listUser.innerHTML = `<strong>Nome: ${user.name}</strong> - Email: ${user.email}`
//             usersList.appendChild(listUser)
//         })
//     })
//     .catch(error => {
//         const errorLi = document.createElement('li');
//         errorLi.innerHTML = `Erro ao carregar usuários: ${error.message}`
//         usersList.appendChild(errorLi)
//     })
// })
import config from "./config.js";
const usersList = document.getElementById('users');

document.addEventListener("DOMContentLoaded", function(){
    fetch(`${config.baseURL}/user`)
    .then(response => response.json())
    .then(data => {
        data.forEach(user => {
            // Criando a estrutura HTML para cada usuário
            const tr = document.createElement('tr');
            
            // Coluna com o checkbox
            const tdCheckbox = document.createElement('td');
            const checkboxSpan = document.createElement('span');
            checkboxSpan.classList.add('custom-checkbox');
            const checkboxInput = document.createElement('input');
            checkboxInput.setAttribute('type', 'checkbox');
            checkboxInput.setAttribute('id', `checkbox${user.id}`); // Supondo que o usuário tenha um ID
            checkboxInput.setAttribute('name', 'options[]');
            checkboxInput.setAttribute('value', user.id); // Supondo que o valor seja o ID do usuário
            const checkboxLabel = document.createElement('label');
            checkboxLabel.setAttribute('for', `checkbox${user.id}`); // Associa o label ao input
            checkboxSpan.appendChild(checkboxInput);
            checkboxSpan.appendChild(checkboxLabel);
            tdCheckbox.appendChild(checkboxSpan);
            tr.appendChild(tdCheckbox);
            
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
            const editLink = document.createElement('a');
            editLink.setAttribute('href', '#editEmployeeModal');
            editLink.classList.add('edit');
            editLink.setAttribute('data-toggle', 'modal');
            const editIcon = document.createElement('i');
            editIcon.classList.add('material-icons');
            editIcon.setAttribute('data-toggle', 'tooltip');
            editIcon.setAttribute('title', 'Edit');
            editIcon.textContent = '\u{E254}'; // Unicode para o ícone
            editLink.appendChild(editIcon);
            tdActions.appendChild(editLink);
            
            const deleteLink = document.createElement('a');
            deleteLink.setAttribute('href', '#deleteEmployeeModal');
            deleteLink.classList.add('delete');
            deleteLink.setAttribute('data-toggle', 'modal');
            const deleteIcon = document.createElement('i');
            deleteIcon.classList.add('material-icons');
            deleteIcon.setAttribute('data-toggle', 'tooltip');
            deleteIcon.setAttribute('title', 'Delete');
            deleteIcon.textContent = '\u{E872}'; // Unicode para o ícone
            deleteLink.appendChild(deleteIcon);
            tdActions.appendChild(deleteLink);
            
            tr.appendChild(tdActions);
            
            // Adicionando a linha (tr) à lista de usuários
            usersList.appendChild(tr);
        })
    })
    .catch(error => {
        const errorTr = document.createElement('tr');
        const errorTd = document.createElement('td');
        errorTd.setAttribute('colspan', '4'); // Colspan para ocupar todas as colunas
        errorTd.textContent = `Erro ao carregar usuários: ${error.message}`;
        errorTr.appendChild(errorTd);
        usersList.appendChild(errorTr);
    })
})

