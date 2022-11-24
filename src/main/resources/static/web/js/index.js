var app = new Vue({
    el:"#app",
    data:{
        email:"",
        password:"",
        firstName: "",
        lastName: "",
        errorToats:null,
        errorMsg: "",
        showSignUp: false,
    },
    methods:{
        signIn: function(event){
            event.preventDefault();
            axios.post('/api/login',`email=${this.email}&password=${this.password}`)
            .then(() => {
                  this.evaluateRedirect(event);
            })
            .catch(() =>{
                this.errorMsg = "Sign in failed, check the information"   
                this.errorToats.show();
            })
        },
        evaluateRedirect: function(event){
            axios.get("/api/clients/current")
            .then((response) => {
                //console.log(response.data.roles);
                // Redirect con el .length
                /*let hasRoleAdmin = response.data.roles.filter(function(role) {return role == "ADMIN"});
                if (hasRoleAdmin.length > 0) window.location.href="/web/test/clients.html";
                else window.location.href="/web/accounts.html";
                console.log(response.data);*/
                // Redirect con el includes
                if (response.data.roles.includes("ADMIN")) window.location.href="/web/test/clients.html";
                else window.location.href="/web/accounts.html";
            })
            .catch((error)=>{
                // handle error
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        signUp: function(event){
            event.preventDefault();

            axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`)
            .then(() => { this.signIn(event) })
            .catch(() =>{
                this.errorMsg = "Sign up failed, check the information"
                this.errorToats.show();
            })
        },
        showSignUpToogle: function(){
            this.showSignUp = !this.showSignUp;
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        }
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    }
})

