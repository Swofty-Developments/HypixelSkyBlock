package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.data.DataMutexService;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointBankData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.List;
import java.util.UUID;

@CommandParameters(aliases = "testmutex",
        description = "Test the data mutex service",
        usage = "/testmutex <global_key> [operation]",
        permission = Rank.STAFF,
        allowsConsole = false)
public class TestMutexCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString globalKey = ArgumentType.String("global_key");
        ArgumentString operation = ArgumentType.String("operation");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String key = context.get(globalKey);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.sendMessage("§eTesting mutex with global key: §f" + key);
            player.sendMessage("§7Performing simple read operation...");

            testMutexRead(player, key);
        }, globalKey);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String key = context.get(globalKey);
            String op = context.get(operation);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.sendMessage("§eTesting mutex with global key: §f" + key);
            player.sendMessage("§7Operation: §f" + op);

            switch (op.toLowerCase()) {
                case "read" -> testMutexRead(player, key);
                case "write" -> testMutexWrite(player, key);
                case "increment" -> testMutexIncrement(player, key);
                case "stress" -> testMutexStress(player, key);
                default -> player.sendMessage("§cInvalid operation! Use: read, write, increment, stress");
            }
        }, globalKey, operation);
    }

    private void testMutexRead(SkyBlockPlayer player, String globalKey) {
        DataMutexService mutexService = new DataMutexService();

        // Create a fake coop with just this player for testing
        List<UUID> testCoop = List.of(player.getUuid());

        mutexService.withSynchronizedData(
                globalKey,
                testCoop,
                SkyBlockDataHandler.Data.BANK_DATA,
                (DatapointBankData.BankData bankData) -> {
                    player.sendMessage("§aSuccessfully read bank data!");
                    player.sendMessage("§7Current balance: §6" + bankData.getAmount());
                    player.sendMessage("§7Balance limit: §6" + bankData.getBalanceLimit());
                    player.sendMessage("§7Transactions: §f" + bankData.getTransactions().size());

                    // Return null to indicate no changes (read-only operation)
                    return null;
                },
                () -> {
                    player.sendMessage("§cFailed to read data with mutex!");
                }
        );
    }

    private void testMutexWrite(SkyBlockPlayer player, String globalKey) {
        DataMutexService mutexService = new DataMutexService();
        List<UUID> testCoop = List.of(player.getUuid());

        mutexService.withSynchronizedData(
                globalKey,
                testCoop,
                SkyBlockDataHandler.Data.BANK_DATA,
                (DatapointBankData.BankData bankData) -> {
                    // Add a test transaction
                    bankData.addTransaction(new DatapointBankData.Transaction(
                            System.currentTimeMillis(),
                            1000.0,
                            "MutexTest"
                    ));

                    player.sendMessage("§aSuccessfully wrote test transaction!");
                    player.sendMessage("§7Added transaction: +1000 coins");
                    player.sendMessage("§7Total transactions: §f" + bankData.getTransactions().size());

                    return bankData; // Return modified data
                },
                () -> {
                    player.sendMessage("§cFailed to write data with mutex!");
                }
        );
    }

    private void testMutexIncrement(SkyBlockPlayer player, String globalKey) {
        DataMutexService mutexService = new DataMutexService();
        List<UUID> testCoop = List.of(player.getUuid());

        mutexService.withSynchronizedData(
                globalKey,
                testCoop,
                SkyBlockDataHandler.Data.BANK_DATA,
                (DatapointBankData.BankData bankData) -> {
                    double oldAmount = bankData.getAmount();
                    bankData.addAmount(100.0);

                    player.sendMessage("§aSuccessfully incremented bank balance!");
                    player.sendMessage("§7Old balance: §6" + oldAmount);
                    player.sendMessage("§7New balance: §6" + bankData.getAmount());

                    return bankData;
                },
                () -> {
                    player.sendMessage("§cFailed to increment data with mutex!");
                }
        );
    }

    private void testMutexStress(SkyBlockPlayer player, String globalKey) {
        player.sendMessage("§eStarting stress test with 10 concurrent operations...");

        DataMutexService mutexService = new DataMutexService();
        List<UUID> testCoop = List.of(player.getUuid());

        // Run 10 concurrent increment operations
        for (int i = 0; i < 10; i++) {
            final int operationNum = i + 1;

            Thread.startVirtualThread(() -> {
                mutexService.withSynchronizedData(
                        globalKey + "_stress",
                        testCoop,
                        SkyBlockDataHandler.Data.BANK_DATA,
                        (DatapointBankData.BankData bankData) -> {
                            // Simulate some processing time
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }

                            double currentAmount = bankData.getAmount();
                            double newAmount = currentAmount + 10.0;
                            bankData.setAmount(newAmount);

                            player.sendMessage("§7Operation " + operationNum + ": §6" + currentAmount + " → §6" + newAmount);

                            return bankData;
                        },
                        () -> {
                            player.sendMessage("§cOperation " + operationNum + " failed!");
                        }
                );
            });
        }

        player.sendMessage("§aStress test initiated! Watch for operation results...");
    }
}