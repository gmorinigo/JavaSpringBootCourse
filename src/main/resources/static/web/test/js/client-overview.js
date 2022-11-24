var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        creditCards: [],
        debitCards: [],
        errorToats: null,
        errorMsg: null,
    },
    methods:{
        getData: function(){
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            axios.get(`/api/clients/${id}`)
            .then((response) => {
                //get client ifo
                this.clientInfo = response.data;
                this.creditCards = response.data.cards.filter(function(card) {
                    return card.type == "CREDIT";
                });
                this.debitCards = response.data.cards.filter(function(card) {
                    return card.type == "DEBIT";
                });
            })
            .catch((error)=>{
                // handle error
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function(){
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(() =>{
                this.errorMsg = "Sign out failed"   
                this.errorToats.show();
            })
        }
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
})