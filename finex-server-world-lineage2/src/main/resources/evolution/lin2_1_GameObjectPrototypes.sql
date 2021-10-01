# --- !Ups

insert into game_object_prototypes(id, "name") values (1, 'test_player') on conflict(id) do nothing;
insert into game_object_component_prototypes(id, component, prototype_id) values
    (1, 'ru.finex.ws.l2.component.base.CoordinateComponent', 1),
    (2, 'ru.finex.ws.l2.component.base.StatusComponent', 1),
    (3, 'ru.finex.ws.l2.component.player.AbnormalComponent', 1),
    (4, 'ru.finex.ws.l2.component.player.ClanComponent', 1),
    (5, 'ru.finex.ws.l2.component.player.ClassComponent', 1),
    (6, 'ru.finex.ws.l2.component.player.ClientComponent', 1),
    (7, 'ru.finex.ws.l2.component.player.CollisionComponent', 1),
    (8, 'ru.finex.ws.l2.component.player.CubicComponent', 1),
    (9, 'ru.finex.ws.l2.component.player.MountComponent', 1),
    (11, 'ru.finex.ws.l2.component.player.PlayerComponent', 1),
    (12, 'ru.finex.ws.l2.component.player.RecommendationComponent', 1),
    (13, 'ru.finex.ws.l2.component.player.SpeedComponent', 1),
    (14, 'ru.finex.ws.l2.component.player.StateComponent', 1),
    (15, 'ru.finex.ws.l2.component.player.StoreComponent', 1),
    (16, 'ru.finex.ws.l2.component.player.VisualEquipComponent', 1),
    (17, 'ru.finex.ws.l2.component.base.ParameterComponent', 1),
    (18, 'ru.finex.ws.l2.component.base.StatComponent', 1)
    on conflict(id) do nothing;

# --- !Downs

delete from game_object_prototypes where id = 1;