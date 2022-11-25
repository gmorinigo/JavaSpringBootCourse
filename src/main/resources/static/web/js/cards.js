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
            axios.get("/api/clients/current")
            .then((response) => {
                //get client ifo
                this.clientInfo = response.data;
                //this.creditCards = this.clientInfo.cards.filter(card => card.type == "CREDIT");
                //this.debitCards = this.clientInfo.cards.filter(card => card.type == "DEBIT");
                this.creditCards = this.clientInfo.cards.filter(card => card.type == "CREDIT" && card.activeCard);
                this.debitCards = this.clientInfo.cards.filter(card => card.type == "DEBIT" && card.activeCard);
                const fechaHoy = new Date(Date.now());
                const day = fechaHoy.getDate();
                const month = fechaHoy.getMonth();
                const year = fechaHoy.getMonth();
                var fechaHoyForm = fechaHoy.getFullYear() + "-"+ fechaHoy.getMonth()+ "-" + fechaHoy.getDate();

                this.creditCards.forEach(card => {if (card.activeCard) card.activeCard = (fechaHoyForm > card.thruDate)});
                this.debitCards.forEach(card => {if (card.activeCard) {card.activeCard = (fechaHoyForm > card.thruDate)}
                });
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        verifyExpirationDate: function(card){
            console.log(card.activeCard);
            const fechaHoy = new Date(Date.now());
            const day = fechaHoy.getDate();
            const month = fechaHoy.getMonth();
            const year = fechaHoy.getMonth();
            var fechaHoyForm = fechaHoy.getFullYear() + "-"+ fechaHoy.getMonth()+ "-" + fechaHoy.getDate();
            if (card.thruDate > fechaHoyForm) return this.formatDate(card.thruDate)
            else return "Expired";
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
        },
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
})