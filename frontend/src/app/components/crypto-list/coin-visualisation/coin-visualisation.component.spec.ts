import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoinVisualisationComponent } from './coin-visualisation.component';

describe('CoinVisualisationComponent', () => {
  let component: CoinVisualisationComponent;
  let fixture: ComponentFixture<CoinVisualisationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CoinVisualisationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CoinVisualisationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
