import config from "./config.js";

// Pega o corpo (tbody) da tabela principal de exibição de usuários no html (tr_principal)
const usersList = document.getElementById('users');

// Pega a tabela principal para exibir usuários no html
const tr_principal = document.getElementById("tr-principal");

// Pega a opção de usuários ativos no html
const option_active = document.getElementById("option-active");

// Pega a opção de usuários inativos no html
const option_inative = document.getElementById("options-inative-container");

// Guarda o id do usuário clicado quando um click é executado em um dos ícones na tabela (Compras, vendas, cursos, edit ou delete)
var userClicked 


// Faz com que "tr-principal" e "users" sejam limpos para adição de novos dados
function removeChild(){
    while (usersList.firstChild) {
        usersList.removeChild(usersList.firstChild);
    }
    while (tr_principal.firstChild) {
        tr_principal.removeChild(tr_principal.firstChild);
    }
}

// Muda o texto interto e a visibidade das opções de "Ativos / Inativos" para "Voltar"
function changeOptions(){
    option_active.textContent = "Ativos"
    option_inative.style.display = "grid"
}

// Cria a estrutura básica para mostrar os usuários na página
function createStructureUsers(){
    
    const thId = document.createElement("th");
    const thName = document.createElement("th");
    const thEmail = document.createElement("th");

    thId.textContent = "Id";
    thName.textContent = "Nome";
    thEmail.textContent = "Email";
    tr_principal.appendChild(thId);
    tr_principal.appendChild(thName);
    tr_principal.appendChild(thEmail);
}

// Cria cada coluna e seta cada usuário nela
function setUser(user, tr){
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
}

// Cria o ícone de editar
function createEditIcon(tdActions, user){
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
}

//Cria o ícone de deletar
function createDeleteIcon(tdActions, user){
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
}

// Cria o ícone de compras
function createBuyIcon(tdActions, user){
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
}

// Cria o ícone de vendas
function createSellIcon(tdActions, user){
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
}

// Cria o ícone de cursos
function createCourseIcon(tdActions, user){
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
}

// Cria a estrutura e ícones para usuários
function createIconsUsers(tdActions, user){
    createEditIcon(tdActions, user);
    createDeleteIcon(tdActions, user);
    createBuyIcon(tdActions, user);
    createSellIcon(tdActions, user);
    createCourseIcon(tdActions, user);
}

// Cria a lógica de exibição de usuários
function loadUsersLogic(user){
    const tr = document.createElement('tr');
    const tdActions = document.createElement('td');
    setUser(user, tr);
    createIconsUsers(tdActions, user);
    tr.appendChild(tdActions);
    usersList.appendChild(tr);
}

// Faz o request para o backend pegando todos os usuários
function loadUsers() {
    removeChild()
    changeOptions()
    createStructureUsers()

    fetch(`${config.baseURL}/users`)
        .then(response => response.json())
        .then(data => {
            data.filter(user => user.active === true).forEach(user =>{
                loadUsersLogic(user)
            });
        })
        .catch(error => {
            alert(error.message)
        });
}

// Carrega os usuários inativos (deletados)
function loadUsersInativeLogic(user){
    const tr = document.createElement('tr');
    const tdActions = document.createElement('td');
    setUser(user, tr);
    tr.appendChild(tdActions);
    usersList.appendChild(tr);
}

// Carrega os usuários ativos (Não deletados) 
function loadUsersInative() {
    removeChild()
    changeOptions()
    createStructureUsers();

    fetch(`${config.baseURL}/users`)
        .then(response => response.json())
        .then(data => {
            data.filter(user => user.active === false).forEach(user =>{
                loadUsersInativeLogic(user);
            });
        })
        .catch(error => {
            alert(error.message)
        });
}

// Adicionando as duas funções para o contexto do documento
document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('div[onclick="loadUsersInative()"]').addEventListener('click', loadUsersInative);
});

document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('div[onclick="loadUsers()"]').addEventListener('click', loadUsers);
});

// Retorna uma mensagem quando um usuário não possui cursos
function noCourses(){
    const h3 = document.createElement("h3");
    h3.textContent = "Não há cursos";
    tr_principal.appendChild(h3);
}

// Seta os cursos de um usuário na tabela
function setCourses(course, tr){
    // Coluna com o ID da compra
    const tdId = document.createElement('td');
    tdId.textContent = course.id;
    tr.appendChild(tdId);

    const tdName = document.createElement('td');
    tdName.textContent = course.name;
    tr.appendChild(tdName);

    // Coluna com o valor da compra
    const tdDescription = document.createElement('td');
    tdDescription.textContent = course.description;
    tr.appendChild(tdDescription);

    // Coluna com o ID do curso
    const tdActive = document.createElement('td');
    tdActive.textContent = course.active;
    tr.appendChild(tdActive);

    const tdPrice = document.createElement('td');
    tdPrice.textContent = course.price;
    tr.appendChild(tdPrice);

    const tdCategory = document.createElement('td');
    tdCategory.textContent = course.category;
    tr.appendChild(tdCategory);
}

// Cria a estrutura da tabela de cursos
function createCoursesStructure(){
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
}

// Implementa a lógica de exibição de cursos de um usuário
function loadCoursesLogic(course){
    const tr = document.createElement('tr');
    setCourses(course, tr)
    
    const tdActions = document.createElement('td');
    tr.appendChild(tdActions);
    usersList.appendChild(tr);
}

function changeOptionsButtons(){
    option_active.textContent = "Voltar"
    option_inative.style.display = "none"
}

// Faz um request para o backend para pegar os cursos de um úsuario
function loadCourses(userId) {
    removeChild()
    fetch(`${config.baseURL}/courses/user/${userId}`)
        .then(response => response.json())
        .then(courses => {
            if (!courses || courses.length === 0) {
                noCourses();
            } else {       
            createCoursesStructure()
            courses.forEach(course => {
                loadCoursesLogic(course);     
            });
        }
        changeOptionsButtons()
    })
        .catch(error => {
            alert(error.message)
        });
}

// Retorna uma mensagem quando um usuário não possui vendas
function noSales(){
    const h3 = document.createElement("h3");
    h3.textContent = "Não há vendas";
    tr_principal.appendChild(h3);
}

// Cria a estrutura da tabela de vendas
function createSalesStructure(){
    const thId = document.createElement("th");
    const thDate = document.createElement("th");
    const thValue = document.createElement("th");
    thDate.textContent = "Data";
    thValue.textContent = "Valor";
    thId.textContent = "Id";
    tr_principal.appendChild(thId);
    tr_principal.appendChild(thDate);
    tr_principal.appendChild(thValue);
}

// Seta as vendas de um usuário na tabela
function setSales(sale, tr){
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

}

// Implementa a lógica de exibição das vendas
function loadSalesLogic(sale){
    // Criando a estrutura HTML para cada compra
    const tr = document.createElement('tr');

    setSales(sale, tr)
    
    // Coluna com os ícones de ação (editar e deletar)
    const tdActions = document.createElement('td');
    // Adicione seus ícones de ação aqui, se necessário
    tr.appendChild(tdActions);

    // Adicionando a linha (tr) à lista de usuários
    usersList.appendChild(tr);
}

// Faz o request para o back pegando as vendas de um usuário
function loadSales(userId) {
    removeChild()
    fetch(`${config.baseURL}/sales/user/${userId}`)
        .then(response => response.json())
        .then(sales => {
            if (!sales || sales.length === 0) {
                noSales();
            } else {       
            createSalesStructure();
            sales.forEach(sale => {
                loadSalesLogic(sale)
            });
        }
        changeOptionsButtons()
    })
        .catch(error => {
            alert(error.message)
        });
}

// Retorna uma mensagem quando um usuário não possui compras
function noPurchases(){
    const h3 = document.createElement("h3");
    h3.textContent = "Não há compras";
    tr_principal.appendChild(h3);
}

// Cria a estrutura da tabela de compras
function createPurchasesStructure(){
    const thId = document.createElement("th");
    const thDate = document.createElement("th");
    const thCourse = document.createElement("th");
    const thValue = document.createElement("th");
    thDate.textContent = "Data";
    thValue.textContent = "Valor";
    thCourse.textContent = "Curso"
    thId.textContent = "Id";
    tr_principal.appendChild(thId);
    tr_principal.appendChild(thDate);
    tr_principal.appendChild(thCourse)
    tr_principal.appendChild(thValue);
}

// Seta as compras de um usuário
function setPurchases(purchase, tr){

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

    // Coluna com o id do curso
    const tdCourse = document.createElement('td');
    tdCourse.textContent = purchase.course.id;
    tr.appendChild(tdCourse);

    // Coluna com o valor da compra
    const tdValue = document.createElement('td');
    tdValue.textContent = purchase.value;
    tr.appendChild(tdValue);
    

}

// Cria a lógica do carregamento de compras
function loadPurchasesLogic(purchase){
    const tr = document.createElement('tr');
    setPurchases(purchase, tr)

    const tdActions = document.createElement('td');
    tr.appendChild(tdActions);
    usersList.appendChild(tr);
}

// Faz o request de compras de um usuário para o endpoint
function loadPurchases(userId) {
    removeChild()
    fetch(`${config.baseURL}/purchases/user/${userId}`)
        .then(response => response.json())
        .then(purchases => {
            if (!purchases || purchases.length === 0) {
                noPurchases();
            } else {       
            createPurchasesStructure();    
            purchases.forEach(purchase => {
                loadPurchasesLogic(purchase);
            });
        }
        changeOptionsButtons();
    })
        .catch(error => {
            alert(error.message)
        });
}

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

// Carrega os usuários quando a página "/users" é iniciada.
document.addEventListener('DOMContentLoaded', function() {
    loadUsers();
});

// Adicionando um EventListener para o submit do edit user form
document.getElementById('deleteUserForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evita o envio padrão do formulário

    // Chama a função deleteUser() para deletar o usuário clicado
    deleteUser();
});


// Adicionando um EventListener para o submit do edit user form
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

// Adicionando um EventListener para o submit do create user form
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
