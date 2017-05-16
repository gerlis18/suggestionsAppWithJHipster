import { Component, OnInit } from '@angular/core';
import { User } from '../../shared/user/user.model';
import { Principal } from '../../shared/auth/principal.service';

@Component({
  selector: 'jhi-profile',
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

  currentAccount: User;
  constructor(
    private principal: Principal
  ) { }

  ngOnInit() {
    this.principal.identity().then((account) => {
            this.currentAccount = account;
            console.log(this.currentAccount);
        });
  }

}
