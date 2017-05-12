import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../shared/auth/account.service';
import { User } from '../../shared/user/user.model';

@Component({
  selector: 'jhi-profile',
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

  currentAccount: User;
  constructor(
    private accountService: AccountService
  ) { }

  ngOnInit() {
    this.accountService.get().subscribe(
      (user) => {
        this.currentAccount = user;
        console.log(this.currentAccount);
      }
    );
  }

}
